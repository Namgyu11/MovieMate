package com.example.moviemate.user.service;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import com.example.moviemate.user.dto.UserDto;
import com.example.moviemate.user.entity.User;
import com.example.moviemate.user.entity.type.UserType;
import com.example.moviemate.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
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
class UserServiceImplTest {

  @InjectMocks
  private UserServiceImpl userService;

  @Mock
  private UserRepository userRepository;

  @Mock
  private PasswordEncoder passwordEncoder;

  private User user;

  @BeforeEach
  void setUp() {
    user = User.builder()
        .id(1L)
        .email("test1234@test.com")
        .password("test1234")
        .nickname("김민수")
        .phoneNumber("010-1234-5678")
        .userType(UserType.USER)
        .build();
  }

  @Test
  @DisplayName("회원정보 조회 성공")
  void findUser_Success() {
    //given
    given(userRepository.findByEmail(anyString()))
        .willReturn(Optional.of(user));

    //when
    UserDto userDto = userService.userInfoInquiry("test1234@test.com");

    //then
    Assertions.assertEquals(user.getId(), userDto.getId());
  }

}
























