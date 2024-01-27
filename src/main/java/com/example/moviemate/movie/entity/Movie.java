package com.example.moviemate.movie.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Movie {

  @Id
  private String id;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  private String actors;

  @Column(nullable = false)
  private String directors;

  @Column(nullable = false)
  private String genre;
}
