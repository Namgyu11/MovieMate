package com.example.moviemate.auth.service;

import static com.example.moviemate.global.exception.type.ErrorCode.*;

import com.example.moviemate.auth.dto.SignInDto;
import com.example.moviemate.auth.dto.SignUpDto;
import com.example.moviemate.global.exception.GlobalException;
import com.example.moviemate.user.entity.User;
import com.example.moviemate.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;

  @Override
  public SignUpDto signUp(SignUpDto request) {
    if (userRepository.existsByEmail(request.getEmail())) {
      throw new GlobalException(DUPLICATE_USER);
    }

    User savedUser = userRepository.save(SignUpDto.signUpForm(request,passwordEncoder));

    return SignUpDto.fromEntity(savedUser);
  }

  @Override
  public void signIn(SignInDto request) {
    User user = userRepository.findByEmail(request.getEmail())
        .orElseThrow(() -> new GlobalException(USER_NOT_FOUND));

    if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
      throw new GlobalException(PASSWORD_NOT_MATCH);
    }
  }
}
