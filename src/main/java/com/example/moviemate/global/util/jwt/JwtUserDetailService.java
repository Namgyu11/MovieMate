package com.example.moviemate.global.util.jwt;

import static com.example.moviemate.global.exception.type.ErrorCode.USER_NOT_FOUND;

import com.example.moviemate.global.exception.GlobalException;
import com.example.moviemate.global.util.jwt.dto.CustomUserDto;
import com.example.moviemate.user.entity.User;
import com.example.moviemate.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtUserDetailService implements UserDetailsService {

  private final UserRepository userRepository;
  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new GlobalException(USER_NOT_FOUND));
    return new JwtUserDetails(CustomUserDto.fromEntity(user));
  }
}
