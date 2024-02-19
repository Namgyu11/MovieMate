package com.example.moviemate.bookmark.dto;


import com.example.moviemate.bookmark.entity.Bookmark;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookmarkResponse {

  private Long userId;
  private String movieCd;
  private String movieNm;
  private String openDt;
  private String genre;

  public static BookmarkResponse fromEntity(Bookmark bookmark){
    return BookmarkResponse.builder()
        .userId(bookmark.getUser().getId())
        .movieCd(bookmark.getMovie().getId())
        .movieNm(bookmark.getMovie().getMovieNm())
        .openDt(bookmark.getMovie().getOpenDt())
        .genre(bookmark.getMovie().getGenre())
        .build();
  }
}
