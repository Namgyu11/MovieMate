package com.example.moviemate.movie.service;


import com.example.moviemate.global.exception.GlobalException;
import com.example.moviemate.global.exception.type.ErrorCode;
import com.example.moviemate.global.service.RedisService;
import com.example.moviemate.movie.config.MovieApi;
import com.example.moviemate.movie.dto.MovieInfoResponse;
import com.example.moviemate.movie.dto.api.movieDetails.ActorDetailInfo;
import com.example.moviemate.movie.dto.api.movieDetails.MovieDetailInfo;
import com.example.moviemate.movie.dto.api.movieDetails.MovieDetailInfoApiResponse;
import com.example.moviemate.movie.repository.MovieRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 영화 정보를 관리하는 서비스 클래스.
 * 영화 정보를 조회하고, API 응답을 변환하며, 캐시에 데이터를 저장하는 기능을 담당.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MovieInfoService {

  private final MovieRepository movieRepository;

  private final MovieApi movieApi;

  private final RedisService redisService;
  private static final Long SEARCH_DATA_EXPIRATION = 24L * 60 * 60 * 1000; // 24시간 동안 검색 데이터 유지


  @Value("${spring.data.movie.apiKey}")
  private String key;

  /**
   * API 응답을 MovieInfoResponse 객체로 변환.
   * 영화 정보와 배우 정보를 포함.
   *
   * @param response MovieDetailInfoApiResponse 객체
   * @return 변환된 MovieInfoResponse 객체
   */
  public MovieInfoResponse convertApiDataToMovieInfoResponse(MovieDetailInfoApiResponse response) {

    MovieDetailInfo movieApiInfo = response.getMovieInfoResult().getMovieInfo();
    List<ActorDetailInfo> limitedActors = movieApiInfo.getActors().size() > 5 ?
        movieApiInfo.getActors().subList(0, 5) : movieApiInfo.getActors();

    return MovieInfoResponse.builder()
        .movieCd(movieApiInfo.getMovieCd())
        .movieNm(movieApiInfo.getMovieNm())
        .movieNmEn(movieApiInfo.getMovieNmEn())
        .showTm(movieApiInfo.getShowTm())
        .openDt(movieApiInfo.getOpenDt())
        .nations(movieApiInfo.getNations())
        .genres(movieApiInfo.getGenres())
        .directors(movieApiInfo.getDirectors())
        .actors(limitedActors)
        .build();
  }

  /**
   * API 를 통해 영화 정보를 조회하고, 조회된 데이터를 MovieInfoResponse 객체로 변환.
   *
   * @param id 조회할 영화의 id
   * @return 변환된 MovieInfoResponse 객체
   */
  public MovieInfoResponse getMovieInfoData(String id) {
    log.info("API == getMovieList");

    MovieDetailInfoApiResponse response = movieApi.getApiSearchMovieInfoById(key, id);

    return convertApiDataToMovieInfoResponse(response);
  }

  /**
   * 영화 정보를 조회하고, Redis 에 데이터를 저장.
   * Redis 에 해당 영화 정보가 없을 경우 API 를 통해 새로운 정보를 조회하고 저장.
   *
   * @param id 조회하고자 하는 영화의 id
   * @return 조회된 영화 정보를 담은 MovieInfoResponse 객체
   * @throws Exception 영화 정보를 찾을 수 없을 때 발생
   */
  @Transactional
  public MovieInfoResponse findMovieInfoById(String id) throws Exception {

    if (movieRepository.existsById(id)) {
      log.info("search Redis == findMovieInfoById");
      MovieInfoResponse movieInfoResponse
          = redisService.getClassData("movieInfo::" + id, MovieInfoResponse.class);

      // Redis 에 데이터가 없을 경우 API 에서 새로운 영화 정보 가져오기
      if (movieInfoResponse == null) {
        log.info("No relevant data in Redis == findMovieInfoById");

        // API 에서 데이터 가져오기
        movieInfoResponse = getMovieInfoData(id);

        // 조회한 데이터 캐시에 저장
        redisService.setClassData("movieInfo::" + id, movieInfoResponse, SEARCH_DATA_EXPIRATION);
        log.info("Data storage successful in Redis == findMovieInfoById");
      }

      return movieInfoResponse;

    } else {
      throw new GlobalException(ErrorCode.MOVIE_NOT_FOUND);
    }
  }

}
