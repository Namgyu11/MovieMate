package com.example.moviemate.movie.service;

import com.example.moviemate.global.exception.GlobalException;
import com.example.moviemate.global.exception.type.ErrorCode;
import com.example.moviemate.global.service.RedisService;
import com.example.moviemate.movie.config.MovieApi;
import com.example.moviemate.movie.dto.PeopleInfoResponse;
import com.example.moviemate.movie.dto.api.peopleDetails.FilmosList;
import com.example.moviemate.movie.dto.api.peopleDetails.PeopleDetailInfo;
import com.example.moviemate.movie.dto.api.peopleDetails.PeopleDetailInfoApiResponse;
import com.example.moviemate.movie.repository.PeopleRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 이 클래스는 영화인 정보를 조회하고, API 의 응답을 변환하며, Redis 와의 상호작용을 관리.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PeopleInfoService {

  private final PeopleRepository peopleRepository;

  private final MovieApi movieApi;

  private final RedisService redisService;
  private static final Long SEARCH_DATA_EXPIRATION = 24L * 60 * 60 * 1000; // 24시간 동안 검색 데이터 유지

  @Value("${spring.data.movie.apiKey}")
  private String key;

  /**
   * API 응답을 PeopleInfoResponse 객체로 변환.
   * API 에서 받은 영화 인물 정보를 PeopleInfoResponse 객체로 변환하여 반환.
   *
   * @param response API 응답 객체
   * @return 변환된 PeopleInfoResponse 객체
   */
  public PeopleInfoResponse convertApiDataToPeopleInfoResponse(
      PeopleDetailInfoApiResponse response) {

    PeopleDetailInfo peopleApiInfo = response.getPeopleDetailInfoResult().getPeopleDetailInfo();
    List<FilmosList> limitedfilmosList = peopleApiInfo.getFilmos().size() > 5 ?
        peopleApiInfo.getFilmos().subList(0, 5) : peopleApiInfo.getFilmos();

    return PeopleInfoResponse.builder()
        .peopleCd(peopleApiInfo.getPeopleCd())
        .peopleNm(peopleApiInfo.getPeopleNm())
        .peopleNmEn(peopleApiInfo.getPeopleNmEn())
        .sex(peopleApiInfo.getSex())
        .repRoleNm(peopleApiInfo.getRepRoleNm())
        .filmos(limitedfilmosList)
        .build();
  }

  /**
   * API 를 호출하고, 가져온 데이터를 변환.
   *
   * @param id 검색할 영화 인물 ID
   * @return 변환된 PeopleInfoResponse 객체
   */
  public PeopleInfoResponse getPeopleInfoData(String id) {
    log.info("API == getPeopleList");

    PeopleDetailInfoApiResponse response = movieApi.getApiSearchPeopleInfoById(key, id);

    return convertApiDataToPeopleInfoResponse(response);
  }

  /**
   * 영화 인물 ID로 영화 인물 정보를 검색하고, 결과를 반환.
   * Redis 에서 데이터를 먼저 검색하고, 없을 경우 API 를 호출하여 새로운 데이터를 가져오고 Redis 에 저장.
   *
   * @param id 검색할 영화 인물 ID
   * @return 검색된 영화 인물 정보
   * @throws Exception 영화 인물 정보를 찾을 수 없을 때 발생
   */
  @Transactional
  public PeopleInfoResponse findPeopleInfoById(String id) throws Exception {

    if (peopleRepository.existsById(id)) {
      log.info("search Redis == findPeopleInfoById");
      PeopleInfoResponse peopleInfoResponse =
          redisService.getClassData("peopleInfo::" + id, PeopleInfoResponse.class);

      // Redis 에 데이터가 없을 경우 API 에서 새로운 영화 정보 가져오기
      if (peopleInfoResponse == null) {
        log.info("search API == findPeopleInfoById");

        // API 에서 데이터 가져오기
        peopleInfoResponse = getPeopleInfoData(id);
        // 조회한 데이터 캐시에 저장

        redisService.setClassData("peopleInfo::" + id, peopleInfoResponse,
            SEARCH_DATA_EXPIRATION);
        log.info("Data storage successful in Redis == findPeopleInfoById");
      }
      return peopleInfoResponse;
    } else {
      throw new GlobalException(ErrorCode.PEOPLE_NOT_FOUND);
    }
  }

}
