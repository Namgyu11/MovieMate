package com.example.moviemate.post.service.impl;

import static com.example.moviemate.global.exception.type.ErrorCode.*;

import com.example.moviemate.global.exception.GlobalException;

import com.example.moviemate.post.dto.PostCategoryRequest;
import com.example.moviemate.post.dto.PostCategoryResponse;
import com.example.moviemate.post.dto.PostResponse;
import com.example.moviemate.post.entity.PostCategory;
import com.example.moviemate.post.repository.PostRepository;
import com.example.moviemate.post.repository.impl.PostCategoryRepository;
import com.example.moviemate.post.service.PostCategoryService;
import com.example.moviemate.user.entity.User;
import com.example.moviemate.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostCategoryServiceImpl implements PostCategoryService {

  private final UserRepository userRepository;
  private final PostRepository postRepository;
  private final PostCategoryRepository postCategoryRepository;

  @Override
  @Transactional
  public PostCategoryResponse createCategory(String username, PostCategoryRequest request) {
    User user = userRepository.findByEmail(username)
        .orElseThrow(() -> new GlobalException(USER_NOT_FOUND));

    PostCategory postCategory = request.toEntity(user);
    return PostCategoryResponse.fromEntity(postCategoryRepository.save(postCategory));
  }

  @Override
  @Transactional
  public PostCategoryResponse updateCategory(Long id, String username, PostCategoryRequest request) {
    return userRepository.findByEmail(username).map(e -> {
      PostCategory postCategory = postCategoryRepository.findById(id)
          .orElseThrow(() -> new GlobalException(POST_CATEGORY_NOT_FOUND));

      postCategory.changeCategoryName(request.name());
      return PostCategoryResponse.fromEntity(postCategory);
    }).orElseThrow(() -> new GlobalException(USER_NOT_FOUND));
  }

  @Override
  @Transactional
  public Page<PostResponse> getCategoryPosts(Long id, Pageable pageable) {

    postCategoryRepository.findById(id)
        .orElseThrow(() -> new GlobalException(POST_CATEGORY_NOT_FOUND));

    return postRepository.findPostsByCategoryId(id, pageable)
        .map(PostResponse::fromEntity);
  }
}
