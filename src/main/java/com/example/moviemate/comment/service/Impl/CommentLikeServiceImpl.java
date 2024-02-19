package com.example.moviemate.comment.service.Impl;


import static com.example.moviemate.global.exception.type.ErrorCode.*;

import com.example.moviemate.comment.dto.CommentLikeResponse;
import com.example.moviemate.comment.entity.Comment;
import com.example.moviemate.comment.entity.CommentLike;
import com.example.moviemate.comment.repository.CommentLikeRepository;
import com.example.moviemate.comment.repository.CommentRepository;
import com.example.moviemate.comment.service.CommentLikeService;
import com.example.moviemate.global.exception.GlobalException;
import com.example.moviemate.user.entity.User;
import com.example.moviemate.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentLikeServiceImpl implements CommentLikeService {


  private final UserRepository userRepository;
  private final CommentRepository commentRepository;
  private final CommentLikeRepository commentLikeRepository;

  @Override
  @Transactional
  public CommentLikeResponse likeComment(Long commentId, String username) {
    if (commentLikeRepository.existsCommentLikeByCommentIdAndUser_Email(commentId, username)) {
      throw new GlobalException(ALREADY_LIKE_COMMENT);
    }
    Comment comment = commentRepository.findById(commentId)
        .orElseThrow(() -> new GlobalException(COMMENT_NOT_FOUND));

    User user = userRepository.findByEmail(username)
        .orElseThrow(() -> new GlobalException(USER_NOT_FOUND));

    CommentLike commentLike = CommentLike.builder().build();

    commentLike.addComment(comment);
    commentLike.addUser(user);
    comment.updateLikeCount();

    commentLikeRepository.save(commentLike);

    return CommentLikeResponse.builder()
        .commentId(commentId)
        .username(username)
        .isLike(true)
        .build();
  }


  @Override
  @Transactional
  public CommentLikeResponse unlikeComment(Long commentId, String username) {
    return commentLikeRepository.findCommentLikeByCommentIdAndUser_Email(commentId, username)
        .map(like -> {
          commentLikeRepository.delete(like);
          commentRepository.findById(commentId).ifPresent(e -> {
            e.removeCommentLike(like);
            e.updateLikeCount();
          });
          return CommentLikeResponse.builder()
              .commentId(commentId)
              .username(username)
              .isLike(false)
              .build();
        }).orElseThrow(() -> new GlobalException(ALREADY_UNLIKE_COMMENT));
  }
}
