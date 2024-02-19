package com.example.moviemate.post.web;


import com.example.moviemate.auth.config.LoginUser;
import com.example.moviemate.post.dto.PostCategoryRequest;
import com.example.moviemate.post.service.PostCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/post/category")
@RequiredArgsConstructor
public class PostCategoryController {

  private final PostCategoryService postCategoryService;

  @PostMapping
  public ResponseEntity<?> createCategory(@LoginUser String username,
      @RequestBody PostCategoryRequest request) {


    return ResponseEntity.status(HttpStatus.CREATED)
        .body(postCategoryService.createCategory(username, request));
  }

  @PutMapping
  public ResponseEntity<?> updateCategory(@RequestParam("id") Long id, @LoginUser String username,
      @RequestBody PostCategoryRequest request) {

    return ResponseEntity.ok(postCategoryService.updateCategory(id, username, request));
  }

  @GetMapping
  public ResponseEntity<?> getCategoryPosts(@RequestParam("id") Long id,
     @PageableDefault(sort = "id", direction = Direction.DESC) Pageable pageable){

    return ResponseEntity.ok(postCategoryService.getCategoryPosts(id, pageable));
  }

}
