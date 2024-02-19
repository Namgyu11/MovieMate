package com.example.moviemate.comment.repository;

import com.example.moviemate.comment.entity.CommentLike;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

  boolean existsCommentLikeByCommentIdAndUser_Email(Long commentId, String username);
  Optional<CommentLike> findCommentLikeByCommentIdAndUser_Email(Long commentId, String username);
}
