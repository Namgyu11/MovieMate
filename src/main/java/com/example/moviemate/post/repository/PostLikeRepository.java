package com.example.moviemate.post.repository;

import com.example.moviemate.post.entity.PostLike;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

  boolean existsPostLikeByPostIdAndUser_Email(Long postId, String username);
  Optional<PostLike> findByPostIdAndUser_Email(Long postId, String username);

}
