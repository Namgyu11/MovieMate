package com.example.moviemate.movie.web;

import com.example.moviemate.movie.dto.MovieInfoResponse;
import com.example.moviemate.movie.dto.MovieListResponse;
import com.example.moviemate.movie.dto.PeopleInfoResponse;
import com.example.moviemate.movie.dto.PeopleListResponse;
import com.example.moviemate.movie.service.MovieInfoService;
import com.example.moviemate.movie.service.MovieListService;
import com.example.moviemate.movie.service.PeopleInfoService;
import com.example.moviemate.movie.service.PeopleListService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/movie")
@RequiredArgsConstructor
public class MovieController {

  private final MovieListService movieListService;
  private final MovieInfoService movieInfoService;
  private final PeopleListService peopleListService;
  private final PeopleInfoService peopleInfoService;

  /**
   * 영화 이름으로 영화 리스트를 조회하는 API
   * @param movieNm 영화 이름
   * @return 영화 리스트
   */
  @GetMapping("/list/movie/{movieNm}")
  public ResponseEntity<?> getMovieList(@PathVariable String movieNm) throws Exception {
    List<MovieListResponse> movies = movieListService.findMovieByName(movieNm);

    return ResponseEntity.ok(movies);
  }

  /**
   * 영화 코드로 영화 정보를 조회하는 API
   * @param movieCd 영화 코드
   * @return 영화 정보
   */
  @GetMapping("/info/movie/{movieCd}")
  public ResponseEntity<?> getMovieInfo(@PathVariable String movieCd) throws Exception {

    MovieInfoResponse movie = movieInfoService.findMovieInfoById(movieCd);

    return ResponseEntity.ok(movie);
  }

  /**
   * 영화인 이름으로 영화인 리스트를 조회하는 API
   * @param peopleNm 영화인 이름
   * @return 영화인 리스트
   */
  @GetMapping("/list/people/{peopleNm}")
  public ResponseEntity<?> getPeopleList(@PathVariable String peopleNm) throws Exception {

    List<PeopleListResponse> peoples = peopleListService.findByPeopleName(peopleNm);

    return ResponseEntity.ok(peoples);
  }

  /**
   * 영화인 코드로 영화인 정보를 조회하는 API
   * @param peopleCd 영화인 코드
   * @return 영화인 정보
   */
  @GetMapping("/info/people/{peopleCd}")
  public ResponseEntity<?> getPeopleInfo(@PathVariable String peopleCd) throws Exception {

    PeopleInfoResponse people = peopleInfoService.findPeopleInfoById(peopleCd);

    return ResponseEntity.ok(people);
  }
}
