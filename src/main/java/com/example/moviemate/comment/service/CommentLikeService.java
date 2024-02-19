package com.example.moviemate.comment.service;

import com.example.moviemate.comment.dto.CommentLikeResponse;

public interface CommentLikeService {
  CommentLikeResponse likeComment(Long commentId, String username);
  CommentLikeResponse unlikeComment(Long commentId, String username);

}
