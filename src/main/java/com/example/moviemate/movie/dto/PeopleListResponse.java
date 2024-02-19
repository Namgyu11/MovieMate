package com.example.moviemate.movie.dto;

import com.example.moviemate.movie.entity.People;
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
public class PeopleListResponse {

  private String peopleCd;
  private String peopleNm;
  private String peopleNmEn;
  private String repRoleNm;
  private String filmoNames;

  public static PeopleListResponse fromEntity(People people) {
    return PeopleListResponse.builder()
        .peopleCd(people.getId())
        .peopleNm(people.getPeopleNm())
        .peopleNmEn(people.getPeopleNmEn())
        .repRoleNm(people.getRepRoleNm())
        .filmoNames(people.getFilmoNames())
        .build();
  }
}
