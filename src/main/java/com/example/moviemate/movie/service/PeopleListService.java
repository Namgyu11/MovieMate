package com.example.moviemate.movie.service;

import com.example.moviemate.global.service.RedisService;
import com.example.moviemate.movie.config.MovieApi;
import com.example.moviemate.movie.dto.PeopleDto;
import com.example.moviemate.movie.dto.PeopleListResponse;
import com.example.moviemate.movie.dto.api.peopleList.PeopleListApiResponse;
import com.example.moviemate.movie.entity.People;
import com.example.moviemate.movie.repository.PeopleRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 영화 인물 리스트와 관련된 비즈니스 로직을 처리
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PeopleListService {

  private final PeopleRepository peopleRepository;

  private final MovieApi movieApi;

  private final RedisService redisService;
  private static final Long SEARCH_DATA_EXPIRATION = 24L * 60 * 60 * 1000; // 24시간 동안 검색 데이터 유지

  @Value("${spring.data.movie.apiKey}")
  private String key;

  /**
   * API 응답을 People 객체로 변환하고, DB에 저장.
   * 이미 DB에 저장된 인물 정보가 있다면 그 정보를 업데이트하고,
   * 그렇지 않은 경우 새로운 People 객체를 생성하여 DB에 저장.
   *
   * @param response API 응답 객체
   * @return DB에 저장된 People 객체의 목록
   */
  public List<People> transformAndSavePeoples(PeopleListApiResponse response) {
    List<People> savedPeoples = new ArrayList<>();

    // DB에 저장된 모든 People 객체를 가져와 Map 에 저장. (Key: 영화인 ID, Value: 인물 객체)
    Map<String, People> existingMoviesMap = peopleRepository.findAll()
        .stream()
        .collect(Collectors.toMap(People::getId, Function.identity()));

    // API 응답에 포함된 모든 영화인에 대해 반복.
    response.getPeopleListResult().getPeopleListInfo().forEach(e -> {
      // API 응답을 기반으로 PeopleDto 객체를 생성.
      PeopleDto peopleDto = PeopleDto.builder()
          .id(e.getPeopleCd())
          .peopleNm(e.getPeopleNm())
          .repRoleNm(e.getRepRoleNm())
          .filmoNames(e.getFilmoNames())
          .build();

      // 이미 DB에 저장된 인물인지 확인.
      People existingPeople = existingMoviesMap.get(peopleDto.getId());
      if (existingPeople != null) {
        // 기존 인물 정보를 업데이트하고 새로운 상태를 false 로 설정.
        existingPeople.updateFromDto(peopleDto);
        existingPeople.setIsNewStatus(false);
        savedPeoples.add(existingPeople);
      } else {
        // 새로운 인물을 생성하고 새로운 상태를 true 로 설정.
        People people = peopleDto.toEntity();
        people.setIsNewStatus(true);
        savedPeoples.add(people);
      }
    });

    // 변환된 인물 객체를 DB에 저장.
    return peopleRepository.saveAll(savedPeoples);
  }

  /**
   * API 를 호출하고, 가져온 데이터를 변환하고 DB에 저장.
   *
   * @param name 검색할 영화 인물 이름
   * @return DB에 저장된 People 객체의 목록
   */
  public List<People> getPeopleListData(String name) {
    log.info("API == getPeopleListData");

    PeopleListApiResponse response = movieApi.getApiSearchPeopleListByName(key, name);

    return transformAndSavePeoples(response);
  }


  /**
   * API 를 통해 영화 인물 리스트를조회하고, 조회된 데이터를 .People 객체로 변환한 후 저장.
   *
   * @param name 조회할 영화의 이름
   * @return 저장된 영화 목록
   */
  @Transactional
  public List<PeopleListResponse> findByPeopleName(String name) throws Exception {
    log.info("search Redis == findByPeopleNm");

    // Redis 에서 데이터 조회
    List<PeopleListResponse> peopleListResponses = redisService
        .getClassListData("peoples::" + name, List.class
            , PeopleListResponse.class);

    // Redis 에 데이터가 없을 경우 API 에서 새로운 영화 정보 가져오기
    if (peopleListResponses == null || peopleListResponses.isEmpty()) {
      log.info("No relevant data in Redis == findByPeopleNm");

      // API 에서 데이터 가져온 후 DB에 저장
      List<People> people = getPeopleListData(name);

      log.info("Data storage successful in DB == findByPeopleNm");

      peopleListResponses = people.stream()
          .map(PeopleListResponse::fromEntity)
          .toList();

      // 조회한 데이터 캐시에 저장
      redisService.setClassData("peoples::" + name, peopleListResponses, SEARCH_DATA_EXPIRATION);
      log.info("Data storage successful in Redis == findByPeopleNm");
    }

    return peopleListResponses;
  }


}
