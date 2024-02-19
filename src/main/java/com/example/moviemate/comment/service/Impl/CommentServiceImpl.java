package com.example.moviemate.comment.service.Impl;

import static com.example.moviemate.global.exception.type.ErrorCode.*;

import com.example.moviemate.comment.dto.CommentRequest;
import com.example.moviemate.comment.dto.CommentResponse;
import com.example.moviemate.comment.entity.Comment;
import com.example.moviemate.comment.repository.CommentRepository;
import com.example.moviemate.comment.service.CommentService;
import com.example.moviemate.global.exception.GlobalException;
import com.example.moviemate.post.entity.Post;
import com.example.moviemate.post.repository.PostRepository;
import com.example.moviemate.user.entity.User;
import com.example.moviemate.user.entity.type.UserType;
import com.example.moviemate.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

  private final CommentRepository commentRepository;
  private final UserRepository userRepository;
  private final PostRepository postRepository;

  @Override
  public CommentResponse createComment(String username, CommentRequest request) {
    User user = userRepository.findByEmail(username)
        .orElseThrow(() -> new GlobalException(USER_NOT_FOUND));

    Post post = postRepository.findById(request.postId())
        .orElseThrow(() -> new GlobalException(POST_NOT_FOUND));

    Comment comment = Comment.builder()
        .content(request.content())
        .build();

    comment.addPostAndMember(post, user);

    return CommentResponse.fromEntity(commentRepository.save(comment));

  }

  @Override
  @Transactional
  public CommentResponse updateComment(Long id, String username, CommentRequest request) {
    postRepository.findById(request.postId())
        .orElseThrow(() -> new GlobalException(POST_NOT_FOUND));

    Comment comment = commentRepository.findById(id)
        .orElseThrow(() -> new GlobalException(COMMENT_NOT_FOUND));

    if(comment.getUser().getEmail().equals(username)){
      throw new GlobalException(WRITE_NOT_YOURSELF);
    }

    comment.changeContent(request.content());

    return CommentResponse.fromEntity(comment);
  }

  @Override
  @Transactional
  public void deleteComment(Long id, String username) {
    Comment comment = commentRepository.findById(id)
        .orElseThrow(() -> new GlobalException(COMMENT_NOT_FOUND));

    validationDeleteComment(comment, username);

    commentRepository.delete(comment);

  }
  private void validationDeleteComment(Comment comment, String username) {
    if (comment.getUser().getUserType() != UserType.ADMIN
        && !comment.getUser().getEmail().equals(username)) {
      throw new GlobalException(WRITE_NOT_YOURSELF);
    }
  }
  @Override
  @Transactional(readOnly = true)
  public Page<CommentResponse> getCommentList(Long postId, Pageable pageable) {
    return commentRepository.findCommentsByPost(postId, pageable)
        .map(CommentResponse::fromEntity);
  }
}
