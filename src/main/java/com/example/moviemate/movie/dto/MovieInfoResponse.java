package com.example.moviemate.movie.dto;

import com.example.moviemate.movie.dto.api.movieDetails.ActorDetailInfo;
import com.example.moviemate.movie.dto.api.movieDetails.DirectorDetailInfo;
import com.example.moviemate.movie.dto.api.movieDetails.GenreDetailInfo;
import com.example.moviemate.movie.dto.api.movieDetails.NationDetailInfo;
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
public class MovieInfoResponse {
  private String movieCd;
  private String movieNm;
  private String movieNmEn;
  private String showTm;
  private String openDt;
  private List<NationDetailInfo> nations;
  private List<GenreDetailInfo> genres;
  private List<DirectorDetailInfo> directors;
  private List<ActorDetailInfo> actors;

}
