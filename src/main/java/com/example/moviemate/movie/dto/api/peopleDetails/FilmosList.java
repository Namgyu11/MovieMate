package com.example.moviemate.movie.dto.api.peopleDetails;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FilmosList {

  @JsonProperty("movieCd")
  private String movieCd;
  @JsonProperty("movieNm")
  private String movieNm;
  @JsonProperty("moviePartNm")
  private String moviePartNm;
}
