package com.example.moviemate.post.service;

import com.example.moviemate.post.dto.PostLikeResponse;

public interface PostLikeService {
  PostLikeResponse likePost(Long postId, String username);
  PostLikeResponse unlikePost(Long postId, String username);

}
