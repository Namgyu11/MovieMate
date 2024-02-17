package com.example.moviemate.movie.dto;

import com.example.moviemate.movie.dto.api.peopleDetails.FilmosList;
import java.util.List;
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
public class PeopleInfoResponse {

  private String peopleCd;
  private String peopleNm;
  private String peopleNmEn;
  private String sex;
  private String repRoleNm;
  private List<FilmosList> filmos;

}
