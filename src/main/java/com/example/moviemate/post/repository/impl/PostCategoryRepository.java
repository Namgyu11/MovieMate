package com.example.moviemate.post.repository.impl;

import com.example.moviemate.post.entity.PostCategory;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostCategoryRepository extends JpaRepository<PostCategory, Long> {

  Optional<PostCategory> findByName(String name);

}
