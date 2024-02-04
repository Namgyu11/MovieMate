package com.example.moviemate.auth.service;

import com.example.moviemate.auth.dto.SignInDto;
import com.example.moviemate.auth.dto.SignUpDto;
import com.example.moviemate.global.util.jwt.dto.TokenDto;

public interface AuthService {

  SignUpDto signUp(SignUpDto request);

  TokenDto signIn(SignInDto request);
}
