package com.example.moviemate.comment.dto;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Builder
public record CommentRequest(
    @NotBlank(message = "게시물 Id는 필수입니다.")
    Long postId,

    @NotBlank(message = "내용은 필수입니다.")
    String content
) {

}
