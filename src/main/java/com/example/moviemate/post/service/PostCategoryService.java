package com.example.moviemate.post.service;

import com.example.moviemate.post.dto.PostCategoryRequest;
import com.example.moviemate.post.dto.PostCategoryResponse;
import com.example.moviemate.post.dto.PostResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostCategoryService {

  PostCategoryResponse createCategory(String username, PostCategoryRequest request);

  PostCategoryResponse updateCategory(Long id, String username, PostCategoryRequest request);

  Page<PostResponse> getCategoryPosts(Long id, Pageable pageable);

}
