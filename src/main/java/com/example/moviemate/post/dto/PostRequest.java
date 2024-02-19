package com.example.moviemate.post.dto;


import com.example.moviemate.post.entity.Post;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PostRequest {

  @NotBlank(message = "제목을 입력해주세요.")
  private String title;

  @NotBlank(message = "내용을 입력해주세요.")
  @Size(max = 500, message = "내용은 500자 이하로 입력해주세요.")
  private String content;

  @NotBlank(message = "카테고리를 선택해주세요.")
  private String category;

  public Post toEntity(){
    return Post.builder()
        .title(title)
        .content(content)
        .build();
  }


}
