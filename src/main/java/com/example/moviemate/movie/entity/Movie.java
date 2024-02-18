package com.example.moviemate.movie.entity;

import com.example.moviemate.movie.dto.MovieDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import lombok.*;
import org.springframework.data.domain.Persistable;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Movie implements Persistable<String> {

  @Id
  private String id;

  @Column(nullable = false)
  private String movieNm;

  @Column
  private String movieNmEn;

  @Column
  private String openDt;

  @Column
  private String genre;

  public void updateFromDto(MovieDto movieDto) {
    this.movieNm = movieDto.getMovieNm();
    this.movieNmEn = movieDto.getMovieNmEn();
    this.openDt = movieDto.getOpenDt();
    this.genre = movieDto.getGenre();
  }

  @Transient
  protected boolean isNew = true;
  @Override
  public String getId() {
    return id;
  }

  @Override
  public boolean isNew() {
    return isNew;
  }

  public void setIsNewStatus(boolean isNew) {
    this.isNew = isNew;
  }

}
