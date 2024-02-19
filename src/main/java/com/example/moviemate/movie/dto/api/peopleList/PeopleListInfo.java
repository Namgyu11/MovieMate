package com.example.moviemate.movie.dto.api.peopleList;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PeopleListInfo {

  @JsonProperty("peopleCd")
  private String peopleCd;

  @JsonProperty("peopleNm")
  private String peopleNm;

  @JsonProperty("peopleNmEn")
  private String peopleNmEn;

  @JsonProperty("repRoleNm")
  private String repRoleNm;

  @JsonProperty("filmoNames")
  private String filmoNames;

}
