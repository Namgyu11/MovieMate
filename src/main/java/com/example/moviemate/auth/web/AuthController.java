package com.example.moviemate.auth.web;

import com.example.moviemate.auth.dto.SignInDto;
import com.example.moviemate.auth.dto.SignUpDto;
import com.example.moviemate.auth.service.AuthService;
import com.example.moviemate.global.util.mail.dto.SendMailRequest;
import com.example.moviemate.global.util.mail.dto.VerifyMailRequest;
import com.example.moviemate.global.util.mail.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

  private final AuthService authService;
  private final MailService mailService;

  @PostMapping("/signUp")
  public ResponseEntity<?> signUpUser(@RequestBody SignUpDto request){
    return ResponseEntity.status(HttpStatus.CREATED).body(authService.signUp(request));
  }

  @PostMapping("/signIn")
  public ResponseEntity<?> signInUser(@RequestBody SignInDto request){
    return ResponseEntity.ok(authService.signIn(request));
  }
  @PostMapping("/mail/certification")
  public ResponseEntity<?> sendCertificationMail(@RequestBody SendMailRequest request){
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(mailService.generateAndDispatchAuthCode(request.getEmail()));
  }
  @PostMapping("/mail/verify")
  public ResponseEntity<?> sendVerifyMail(@RequestBody VerifyMailRequest request){
    mailService.verifyEmail(request.getEmail(), request.getCode());
    return ResponseEntity.status(HttpStatus.OK).build();
  }
}
