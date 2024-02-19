package com.example.moviemate.bookmark.entity;

import com.example.moviemate.global.entity.BaseEntity;
import com.example.moviemate.movie.entity.Movie;
import com.example.moviemate.user.entity.User;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Bookmark extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "movie_id")
  private Movie movie;

  public void addBookmark(User user) {
    this.user = user;
    user.addBookmark(this);
  }

  public void addMovie(Movie movie) {
    this.movie = movie;
  }

}
