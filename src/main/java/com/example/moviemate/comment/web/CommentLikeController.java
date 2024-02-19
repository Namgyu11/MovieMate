package com.example.moviemate.comment.web;


import com.example.moviemate.auth.config.LoginUser;
import com.example.moviemate.comment.service.CommentLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class CommentLikeController {

  private final CommentLikeService commentLikeService;

  @PostMapping("/like")
  public ResponseEntity<?> likeComment(@RequestParam(name = "id") Long commentId
      , @LoginUser String username) {

    return ResponseEntity.ok(commentLikeService.likeComment(commentId, username));
  }

  @PostMapping("/unlike")
  public ResponseEntity<?> unlikeComment(@RequestParam(name = "id") Long commentId,
      @LoginUser String username) {
    return ResponseEntity.ok(commentLikeService.unlikeComment(commentId, username));
  }
}
