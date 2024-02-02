package com.example.moviemate.auth.service;

import com.example.moviemate.auth.dto.SignInDto;
import com.example.moviemate.auth.dto.SignUpDto;

public interface AuthService {

  SignUpDto signUp(SignUpDto request);

  void signIn(SignInDto request);
}
