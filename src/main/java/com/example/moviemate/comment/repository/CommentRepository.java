package com.example.moviemate.comment.repository;

import com.example.moviemate.comment.entity.Comment;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

  @Query("select c from Comment c inner join Post p on c.post.id = p.id "
  +" where p.id = :postId")
  Page<Comment> findCommentsByPost(@Param("postId") Long postId, Pageable pageable);

}
