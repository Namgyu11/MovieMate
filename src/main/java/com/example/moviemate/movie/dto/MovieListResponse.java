package com.example.moviemate.movie.dto;

import com.example.moviemate.movie.entity.Movie;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class MovieListResponse {

  private String movieCd;
  private String movieNm;
  private String movieNmEn;
  private String openDt;
  private String genre;

  public static MovieListResponse fromEntity(Movie Movie) {
    return MovieListResponse.builder()
        .movieCd(Movie.getId())
        .movieNm(Movie.getMovieNm())
        .movieNmEn(Movie.getMovieNmEn())
        .openDt(Movie.getOpenDt())
        .genre(Movie.getGenre())
        .build();

  }
}
