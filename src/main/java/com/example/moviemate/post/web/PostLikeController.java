package com.example.moviemate.post.web;


import com.example.moviemate.auth.config.LoginUser;
import com.example.moviemate.post.service.PostLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostLikeController {

  private final PostLikeService postLikeService;

  @PostMapping("/like")
  public ResponseEntity<?> likePost(@RequestParam("id") Long postId, @LoginUser String username) {
    return ResponseEntity.ok(postLikeService.likePost(postId, username));
  }

  @PostMapping("/unlike")
  public ResponseEntity<?> unlikePost(@RequestParam("id") Long postId, @LoginUser String username) {
    return ResponseEntity.ok(postLikeService.unlikePost(postId, username));
  }

}
