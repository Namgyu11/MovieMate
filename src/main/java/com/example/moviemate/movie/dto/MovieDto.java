package com.example.moviemate.movie.dto;

import com.example.moviemate.movie.entity.Movie;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class MovieDto {

  private String id;
  private String movieNm;
  private String movieNmEn;
  private String openDt;
  private String genre;

  public static MovieDto fromEntity(Movie movie) {
    return MovieDto.builder()
        .id(movie.getId())
        .movieNm(movie.getMovieNm())
        .movieNmEn(movie.getMovieNmEn())
        .openDt(movie.getOpenDt())
        .genre(movie.getGenre())
        .build();
  }

  public Movie toEntity() {
    return Movie.builder()
        .id(id)
        .movieNm(movieNm)
        .movieNmEn(movieNmEn)
        .openDt(openDt)
        .genre(genre)
        .build();
  }

}
