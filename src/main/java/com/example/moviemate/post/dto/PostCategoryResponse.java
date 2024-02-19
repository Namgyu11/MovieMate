package com.example.moviemate.post.dto;


import com.example.moviemate.post.entity.PostCategory;
import lombok.Builder;

@Builder
public record PostCategoryResponse(
    Long id,
    Long userId,
    String name
) {

  public static PostCategoryResponse fromEntity(PostCategory postCategory){
    return PostCategoryResponse.builder()
        .id(postCategory.getId())
        .userId(postCategory.getUser().getId())
        .name(postCategory.getName())
        .build();
  }
}
