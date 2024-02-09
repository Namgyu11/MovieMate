package com.example.moviemate.user.web;

import com.example.moviemate.auth.config.LoginUser;
import com.example.moviemate.user.dto.UpdateUserDto;
import com.example.moviemate.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@Slf4j
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @GetMapping
  public ResponseEntity<?> userInfoInquiry(@LoginUser String username) {
    log.info("Username from @LoginUser: {}", username);
    return ResponseEntity.ok(userService.userInfoInquiry(username));
  }

  @PutMapping
  public ResponseEntity<?> userInfoUpdate(@Valid @RequestBody UpdateUserDto updateUserDto,
      @LoginUser String username) {
    return ResponseEntity.ok(userService.updateUserInfo(updateUserDto, username));
  }
}
