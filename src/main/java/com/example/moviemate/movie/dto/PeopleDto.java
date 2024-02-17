package com.example.moviemate.movie.dto;

import com.example.moviemate.movie.entity.People;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class PeopleDto {

  private String id;
  private String peopleNm;
  private String peopleNmEn;
  private String repRoleNm;
  private String filmoNames;

  public People toEntity() {
    return People.builder()
        .id(id)
        .peopleNm(peopleNm)
        .peopleNmEn(peopleNmEn)
        .repRoleNm(repRoleNm)
        .filmoNames(filmoNames)
        .build();
  }
}
