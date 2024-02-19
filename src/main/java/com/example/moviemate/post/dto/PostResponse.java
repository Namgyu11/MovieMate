package com.example.moviemate.post.dto;


import com.example.moviemate.post.entity.Post;
import java.time.LocalDateTime;
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
public class PostResponse {

  private Long id;
  private String title;
  private String content;
  private String username;
  private int views;
  private int likeCount;
  private LocalDateTime createdAt;
  private Long categoryId;


  public static PostResponse fromEntity(Post post){
    return PostResponse.builder()
        .id(post.getId())
        .title(post.getTitle())
        .content(post.getContent())
        .username(post.getUser().getEmail())
        .views(post.getViews())
        .likeCount(post.getLikeCount())
        .createdAt(post.getCreatedAt())
        .categoryId(post.getPostCategory().getId())
        .build();
  }

}
