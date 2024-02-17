package com.example.moviemate.movie.dto.api.peopleList;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PeopleListApiResponse {

  @JsonProperty("peopleListResult")
  private PeopleListResult peopleListResult;
}
