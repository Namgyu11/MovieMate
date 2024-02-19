package com.example.moviemate.comment.service;

import com.example.moviemate.comment.dto.CommentRequest;
import com.example.moviemate.comment.dto.CommentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentService {

  CommentResponse createComment(String username, CommentRequest request);

  CommentResponse updateComment(Long id, String username, CommentRequest request);

  void deleteComment(Long id, String username);

  Page<CommentResponse> getCommentList(Long postId, Pageable pageable);
}
