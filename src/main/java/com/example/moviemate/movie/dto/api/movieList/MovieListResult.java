package com.example.moviemate.movie.dto.api.movieList;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MovieListResult {

  @JsonProperty("movieList")
  private List<MovieListInfo> movieList;

}
