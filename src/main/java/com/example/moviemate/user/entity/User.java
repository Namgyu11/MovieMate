package com.example.moviemate.user.entity;

import com.example.moviemate.bookmark.entity.Bookmark;
import com.example.moviemate.global.entity.BaseEntity;
import com.example.moviemate.post.entity.Post;
import com.example.moviemate.user.entity.type.UserType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  private String nickname;

  @Column(nullable = false)
  private String phoneNumber;

  @Builder.Default
  private boolean emailAuth = false;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private UserType userType;

  @Builder.Default
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Post> posts = new ArrayList<>();

  @Builder.Default
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Bookmark> bookmarks = new ArrayList<>();

  public void changeEmailAuth() {
    this.emailAuth = true;
  }

  public void addBookmark(Bookmark bookmark) {
    this.bookmarks.add(bookmark);
  }


}
