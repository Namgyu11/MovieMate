package com.example.moviemate.movie.dto.api.peopleDetails;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PeopleDetailInfo {
  @JsonProperty("peopleCd")
  private String peopleCd;

  @JsonProperty("peopleNm")
  private String peopleNm;

  @JsonProperty("peopleNmEn")
  private String peopleNmEn;

  @JsonProperty("sex")
  private String sex;

  @JsonProperty("repRoleNm")
  private String repRoleNm;

  @JsonProperty("filmos")
  private List<FilmosList> filmos;
}
