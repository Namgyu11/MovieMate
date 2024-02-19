package com.example.moviemate.bookmark.web;

import com.example.moviemate.auth.config.LoginUser;
import com.example.moviemate.bookmark.dto.BookmarkRequest;
import com.example.moviemate.bookmark.service.BookmarkService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bookmarks")
public class BookmarkController {
  private final BookmarkService bookmarkService;

  @PostMapping
  public ResponseEntity<?> createBookmark(@LoginUser String username,
      @Valid @RequestBody BookmarkRequest request) {

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(bookmarkService.createBookmark(username, request));
  }

  @GetMapping
  public ResponseEntity<?> getBookmarks(@LoginUser String username) {

    return ResponseEntity.ok(bookmarkService.findBookmarks(username));
  }

  @DeleteMapping
  public ResponseEntity<?> deleteBookmark(@LoginUser String username,
      @RequestParam("id") Long id) {
    bookmarkService.deleteBookmark(username, id);
    return ResponseEntity.noContent().build();
  }
}
