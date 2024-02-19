package com.example.moviemate.post.dto;


import com.example.moviemate.post.entity.PostCategory;
import com.example.moviemate.user.entity.User;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record PostCategoryRequest(
    @NotBlank String name) {

  public PostCategory toEntity(User user){
    return PostCategory.builder()
        .name(this.name)
        .user(user)
        .build();
  }

}
