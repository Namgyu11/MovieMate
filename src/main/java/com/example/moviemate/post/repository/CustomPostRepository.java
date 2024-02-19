package com.example.moviemate.post.repository;

import com.example.moviemate.post.entity.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.annotation.Transactional;

public interface CustomPostRepository {

  Slice<Post> searchByTitle(Long postId, String title, Pageable pageable);

  @Transactional
  void updateViews(Long id, int views);

}
