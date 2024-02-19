package com.example.moviemate.movie.dto.api.movieList;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MovieListDirectorsInfo {

  @JsonProperty("peopleNm")
  private String peopleNm;

}
