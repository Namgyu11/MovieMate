package com.example.moviemate.user.service;

import static com.example.moviemate.global.exception.type.ErrorCode.*;

import com.example.moviemate.global.exception.GlobalException;
import com.example.moviemate.user.dto.UpdateUserDto;
import com.example.moviemate.user.dto.UpdateUserResponse;
import com.example.moviemate.user.dto.UserDto;
import com.example.moviemate.user.entity.User;
import com.example.moviemate.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  /**
   * 사용자 정보를 조회하는 메서드
   *
   * @param username 사용자의 이메일
   * @return 사용자의 정보가 담긴 UserDto 객체
   */

  @Override
  @Transactional
  public UserDto userInfoInquiry(String username) {

    return UserDto.fromEntity(userRepository.findByEmail(username)
        .orElseThrow(() -> new GlobalException(USER_NOT_FOUND)));

  }

  /**
   * 사용자 정보 수정
   *
   * @param updateUserDto 업데이트할 사용자 정보가 담긴 객체
   * @param username 업데이트할 사용자의 이메일
   * @return 업데이트된 사용자 정보가 담긴 UpdateUserResponse 객체
   */
  @Override
  @Transactional
  public UpdateUserResponse updateUserInfo(UpdateUserDto updateUserDto, String username) {
    User user = userRepository.findByEmail(username)
        .orElseThrow(() -> new GlobalException(USER_NOT_FOUND));

    user.setPassword(passwordEncoder.encode(updateUserDto.getPassword()));
    user.setNickname(updateUserDto.getNickname());
    user.setPhoneNumber(updateUserDto.getPhoneNumber());

    return UpdateUserResponse.fromEntity(user);
  }


}
