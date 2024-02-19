package com.example.moviemate.movie.service;


import com.example.moviemate.global.service.RedisService;
import com.example.moviemate.movie.config.MovieApi;
import com.example.moviemate.movie.dto.MovieDto;
import com.example.moviemate.movie.dto.MovieListResponse;
import com.example.moviemate.movie.dto.api.movieList.MovieListApiResponse;
import com.example.moviemate.movie.entity.Movie;
import com.example.moviemate.movie.repository.MovieRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

/**
 * 영화 목록을 관리하는 서비스 클래스.
 * 영화 목록을 조회하고, API 응답을 변환하며, 캐시에 데이터를 저장하는 기능을 담당.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MovieListService {

  private final MovieRepository movieRepository;

  private final MovieApi movieApi;

  private final RedisService redisService;
  private static final Long SEARCH_DATA_EXPIRATION = 24L * 60 * 60 * 1000; // 24시간 동안 검색 데이터 유지


  @Value("${spring.data.movie.apiKey}")
  private String key;

//  public void getMovieApiData() {
//    StopWatch stopWatch = new StopWatch();
//    stopWatch.start();
//
//    //getMovieListData("광해");
//
//    stopWatch.stop();
//    log.info(stopWatch.prettyPrint());
//    log.info("API 실행 시간(s): " + stopWatch.getTotalTimeSeconds());
//  }

  /**
   * API 응답을 Movie 객체로 변환하고, DB에 저장.
   * 이미 DB에 저장된 인물 정보가 있다면 그 정보를 업데이트하고,
   * 그렇지 않은 경우 새로운 People 객체를 생성하여 DB에 저장.
   *
   * @param response API 응답 객체
   * @return 저장된 Movie 객체 리스트
   */
  public List<Movie> transformAndSaveMovies(MovieListApiResponse response) {
    List<Movie> savedMovies = new ArrayList<>();

    // DB에 저장된 모든 영화를 가져와 Map 에 저장 (Key: 영화 ID, Value: 영화 객체)
    Map<String, Movie> existingMoviesMap = movieRepository.findAll()
        .stream()
        .collect(Collectors.toMap(Movie::getId, Function.identity()));

    // API 응답에 포함된 모든 영화에 대해 반복
    response.getMovieListResult().getMovieList().forEach(e -> {
      // API 응답을 기반으로 MovieDto 객체 생성
      MovieDto movieDto = MovieDto.builder()
          .id(e.getMovieId())
          .movieNm(e.getMovieNm())
          .movieNmEn(e.getMovieNmEn())
          .openDt(e.getOpenDt())
          .genre(e.getGenreAlt())
          .build();

      // 이미 DB에 저장된 영화인지 확인
      Movie existingMovie = existingMoviesMap.get(movieDto.getId());
      if (existingMovie != null) {

        // 기존 영화 정보를 업데이트하고 새로운 상태를 false로 설정
        existingMovie.updateFromDto(movieDto);
        existingMovie.setIsNewStatus(false);
        savedMovies.add(existingMovie);
      } else {
        // 새로운 영화를 생성하고 새로운 상태를 true로 설정
        Movie movie = movieDto.toEntity();
        movie.setIsNewStatus(true);
        savedMovies.add(movie);
      }
    });

    // 변환된 영화 객체를 DB에 저장
    return movieRepository.saveAll(savedMovies);
  }


  /**
   * API 를 통해 영화 목록을 조회하고, 조회된 데이터를 Movie 객체로 변환한 후 저장.
   *
   * @param name 조회할 영화의 이름
   * @return 저장된 영화 목록
   */
  public List<Movie> getMovieListData(String name) {
    log.info("API == getMovieList");

    MovieListApiResponse response = movieApi.getApiSearchMovieListByName(key, name);

    return transformAndSaveMovies(response);
  }


  /**
   * 영화 이름을 통해 영화 목록을 조회하고, Redis 에 데이터를 저장.
   * Redis 에 해당 영화 목록이 없을 경우 API 를 통해 새로운 정보를 조회하고 저장.
   *
   * @param name 조회하고자 하는 영화의 이름
   * @return 조회된 영화 목록을 담은 MovieListResponse 객체 리스트
   */
  @Transactional
  public List<MovieListResponse> findMovieByName(String name) throws Exception {
    log.info("search Redis == findMovieByName");

    // Redis 에서 데이터 조회
    List<MovieListResponse> movieListResponses = redisService
        .getClassListData("movies::" + name, List.class
            , MovieListResponse.class);

    // Redis 에 데이터가 없을 경우 API 에서 새로운 영화 정보 가져오기
    if (movieListResponses == null || movieListResponses.isEmpty()) {
      log.info("No relevant data in Redis == findMovieByName");

      // API 에서 데이터 가져온 후 DB에 저장
      List<Movie> movies = getMovieListData(name);

      log.info("Data storage successful in DB == findMovieByName");

      movieListResponses = movies.stream()
          .map(MovieListResponse::fromEntity)
          .toList();

      // 조회한 데이터 캐시에 저장
      redisService.setClassData("movies::" + name, movieListResponses, SEARCH_DATA_EXPIRATION);
      log.info("Data storage successful in Redis == findMovieByName");
    }

    return movieListResponses;
  }

}
