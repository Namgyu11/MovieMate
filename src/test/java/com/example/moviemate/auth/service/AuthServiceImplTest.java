package com.example.moviemate.auth.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.example.moviemate.auth.dto.SignUpDto;
import com.example.moviemate.global.exception.GlobalException;
import com.example.moviemate.global.exception.type.ErrorCode;
import com.example.moviemate.global.util.jwt.TokenProvider;
import com.example.moviemate.user.entity.User;
import com.example.moviemate.user.entity.type.UserType;
import com.example.moviemate.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
@Transactional
class AuthServiceImplTest {

  @InjectMocks
  private AuthServiceImpl authService;

  @Mock
  private UserRepository userRepository;
  @Mock
  private TokenProvider tokenProvider;

  @Mock
  private PasswordEncoder passwordEncoder;

  private User user;

  @BeforeEach
  void setup() {
    user = User.builder()
        .email("test1234@test.com")
        .password("test1234")
        .phoneNumber("010-1234-5678")
        .userType(UserType.USER)
        .build();
  }

  @Test
  @DisplayName("회원가입 성공")
  void signUp_success() {
    // given
    SignUpDto signUpDto = SignUpDto.builder()
        .email("test1234@test.com")
        .password(passwordEncoder.encode("test1234"))
        .nickname("김민수")
        .phoneNumber("010-1234-5678")
        .build();

    given(userRepository.save(any()))
        .willReturn(User.builder()
            .email("test1234@test.com")
            .phoneNumber("010-1234-5678")
            .build());

    // when
    SignUpDto signUp = authService.signUp(signUpDto);

    // then
    assertEquals(user.getEmail(), signUp.getEmail());
    assertEquals(user.getPhoneNumber(), signUp.getPhoneNumber());
  }
  @Test
  @DisplayName("회원가입 실패 - 이미 존재하는 이메일")
  void signUp_fail() {
    // given
    SignUpDto signUpDto = SignUpDto.builder()
        .email("test1234@test.com")
        .password(passwordEncoder.encode("test1234"))
        .nickname("김민수")
        .phoneNumber("010-1234-5678")
        .build();

    given(userRepository.existsByEmail(any()))
        .willReturn(true);

    // when
    GlobalException globalException = assertThrows(GlobalException.class,
        () -> authService.signUp(signUpDto));

    // then
    assertEquals(ErrorCode.DUPLICATE_USER, globalException.getErrorCode());
  }

  @Test
  void logout() {
  }

  @Test
  void reissue() {
  }
}