package com.example.moviemate.movie.dto.api.peopleList;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PeopleListResult {

  @JsonProperty("peopleList")
  private List<PeopleListInfo> peopleListInfo;
}
