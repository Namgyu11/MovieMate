package com.example.moviemate.movie.dto.api.movieList;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MovieListInfo {

  @JsonProperty("movieCd")
  private String movieId;

  @JsonProperty("movieNm")
  private String movieNm;

  @JsonProperty("movieNmEn")
  private String movieNmEn;

  @JsonProperty("openDt")
  private String openDt;

  @JsonProperty("genreAlt")
  private String genreAlt;

  @JsonProperty("directors")
  private List<MovieListDirectorsInfo> directors;

}
