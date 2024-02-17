package com.example.moviemate.movie.dto.api.movieDetails;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MovieDetailInfo {

  /**
   * 영화 코드
   */
  @JsonProperty("movieCd")
  private String movieCd;

  /**
   * 영화명(국문)
   */
  @JsonProperty("movieNm")
  private String movieNm;

  /**
   * 영화명(영문)
   */
  @JsonProperty("movieNmEn")
  private String movieNmEn;

  /**
   * 영화명(원문)
   */
  @JsonProperty("showTm")
  private String showTm;

  /**
   * 개봉일
   */
  @JsonProperty("openDt")
  private String openDt;

  /**
   * 국가 정보
   */
  @JsonProperty("nations")
  private List<NationDetailInfo> nations;

  /**
   * 장르 정보
   */
  @JsonProperty("genres")
  private List<GenreDetailInfo> genres;

  /**
   * 감독 정보
   */
  @JsonProperty("directors")
  private List<DirectorDetailInfo> directors;

  /**
   * 배우 정보
   */
  @JsonProperty("actors")
  private List<ActorDetailInfo> actors;
}
