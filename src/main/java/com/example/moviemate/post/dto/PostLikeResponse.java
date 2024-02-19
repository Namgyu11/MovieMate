package com.example.moviemate.post.dto;


import lombok.Builder;

@Builder
public record PostLikeResponse(
    String username,
    Long postId,
    boolean isLike) {

}
