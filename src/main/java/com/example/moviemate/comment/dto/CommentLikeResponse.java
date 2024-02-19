package com.example.moviemate.comment.dto;


import lombok.Builder;

@Builder
public record CommentLikeResponse (
    String username,
    Long commentId,
    boolean isLike
) {

}