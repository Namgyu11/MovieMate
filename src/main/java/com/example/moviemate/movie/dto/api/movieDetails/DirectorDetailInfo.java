package com.example.moviemate.movie.dto.api.movieDetails;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DirectorDetailInfo {

  @JsonProperty("peopleNm")
  private String peopleNm;

  @JsonProperty("peopleNmEn")
  private String peopleNmEn;

}
