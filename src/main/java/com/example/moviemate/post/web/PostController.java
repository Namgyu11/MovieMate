package com.example.moviemate.post.web;


import com.example.moviemate.auth.config.LoginUser;
import com.example.moviemate.post.dto.PostRequest;
import com.example.moviemate.post.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostController {

  private final PostService postService;

  @PostMapping
  public ResponseEntity<?> createPost(@Valid @RequestPart(value = "request") PostRequest requestDto,
      @LoginUser String username,
      @RequestPart(value = "files", required = false) List<MultipartFile> files) {

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(postService.createPost(requestDto, username, files));
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> updatePost(@PathVariable Long id,
      @Valid @RequestBody PostRequest requestDto,
      @LoginUser String username) {
    return ResponseEntity.ok(postService.updatePost(id, requestDto, username));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deletePost(@PathVariable Long id,
      @LoginUser String username) {
    postService.deletePost(id, username);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @GetMapping("/detail/{id}")
  public ResponseEntity<?> getPostsDetail(@PathVariable Long id, HttpServletRequest request) {
    return ResponseEntity.ok(postService.readPost(id, request));
  }

  @GetMapping("/searchAll")
  public ResponseEntity<?> getPostList(@PageableDefault(sort = "createdAt"
      , direction = Direction.DESC) Pageable pageable) {
    return ResponseEntity.ok(postService.findPosts(pageable));
  }

  @GetMapping("/search/title")
  public ResponseEntity<?> searchPostTitle(@RequestParam("id") Long id,
      @RequestParam("name") String name,
      @PageableDefault(sort = "createdAt", direction = Direction.DESC) Pageable pageable) {

    return ResponseEntity.ok(postService.searchTitle(id, name, pageable));
  }

  @GetMapping("/search/content")
  public ResponseEntity<?> getPostContent(@RequestParam("name") String name,
      @PageableDefault(sort = "createdAt", direction = Direction.DESC) Pageable pageable) {

    return ResponseEntity.ok(postService.searchContent(name, pageable));
  }

}
