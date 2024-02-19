package com.example.moviemate.comment.dto;


import com.example.moviemate.comment.entity.Comment;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentResponse {

  private Long userId;
  private Long postId;
  private String content;

  public static CommentResponse fromEntity(Comment comment) {
    return CommentResponse.builder()
        .userId(comment.getUser().getId())
        .postId(comment.getPost().getId())
        .content(comment.getContent())
        .build();
  }
}
