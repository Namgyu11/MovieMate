package com.example.moviemate.auth.service;

import static com.example.moviemate.global.exception.type.ErrorCode.*;

import com.example.moviemate.auth.dto.LogoutDto;
import com.example.moviemate.auth.dto.ReissueDto;
import com.example.moviemate.auth.dto.SignInDto;
import com.example.moviemate.auth.dto.SignUpDto;
import com.example.moviemate.global.exception.GlobalException;
import com.example.moviemate.global.service.RedisService;
import com.example.moviemate.global.util.jwt.TokenProvider;
import com.example.moviemate.global.util.jwt.dto.TokenDto;
import com.example.moviemate.user.entity.User;
import com.example.moviemate.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;

  private final TokenProvider tokenProvider;
  private final RedisService redisService;

  private static final String REFRESH_TOKEN_PREFIX = "REFRESH_TOKEN: ";

  @Override
  public SignUpDto signUp(SignUpDto request) {
    if (userRepository.existsByEmail(request.getEmail())) {
      throw new GlobalException(DUPLICATE_USER);
    }

    String encodedPasswordEncoder = passwordEncoder.encode(request.getPassword());
    User savedUser = userRepository.save(SignUpDto.signUpForm(request, encodedPasswordEncoder));

    return SignUpDto.fromEntity(savedUser);
  }

  @Override
  public TokenDto signIn(SignInDto request) {
    User user = userRepository.findByEmail(request.getEmail())
        .orElseThrow(() -> new GlobalException(USER_NOT_FOUND));

    if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
      throw new GlobalException(PASSWORD_NOT_MATCH);
    }

    if (!user.isEmailAuth()) {
      throw new GlobalException(EMAIL_NOT_VERITY);
    }
    return generateToken(user.getEmail(), user.getUserType().getCode());
  }

  /**
   * 사용자의 로그아웃 요청을 처리
   *
   * @param request 로그아웃을 요청한 사용자의 accessToken 정보를 담은 객체
   */
  @Override
  @Transactional
  public void logout(LogoutDto request) {
    // 제공된 액세스 토큰의 유효성을 검증.
    if (!tokenProvider.validateToken(request.getAccessToken())) {
      throw new GlobalException(INVALID_TOKEN);
    }

    //  토큰이 유효하다면, Redis 에서 해당 토큰 정보를 삭제.
    if (redisService.getData(REFRESH_TOKEN_PREFIX + request.getAccessToken()) != null) {
      redisService.deleteData(REFRESH_TOKEN_PREFIX + request.getAccessToken());
    }

    // 액세스 토큰의 유효 시간을 확인하고 로그아웃한 토큰을 블랙리스트에 등록.
    Long expiredTime = tokenProvider.getExpireTime(request.getAccessToken());
    redisService.setDataExpire(REFRESH_TOKEN_PREFIX + request.getAccessToken(),
        "Logout", expiredTime);
  }

  /**
   * 기존의 토큰이 유효하지 않아 새로운 토큰을 발행하는 경우에 사용.
   *
   * @param reissueDto 새로운 토큰을 발행하기 위한 정보를 담고 있는 객체
   * @return 새로 발행된 토큰 정보를 담은 TokenDto 객체
   */
  @Override
  public TokenDto reissue(ReissueDto reissueDto) {

    // Redis 에서 기존의 액세스 토큰을 키로 가지는 데이터를 가져옴.
    String data = redisService.getData(REFRESH_TOKEN_PREFIX + reissueDto.getAccessToken());

    // 저장된 데이터가 없거나, 저장된 Refresh 토큰과 요청된 Refresh 토큰이 일치하지 않으면 예외를 발생.
    if (!StringUtils.hasText(data) || !data.equals(reissueDto.getRefreshToken())) {
      throw new GlobalException(INVALID_TOKEN);
    }

    // 액세스 토큰을 이용해 사용자의 인증 정보를 가져옴.
    Authentication authentication = tokenProvider.getAuthentication(reissueDto.getAccessToken());

    // Redis 에서 기존의 액세스 토큰을 삭제.
    redisService.deleteData(data);

    //인증 정보를 이용해 새로운 토큰을 발행하고, 그 토큰 정보를 담은 TokenDto 객체를 반환
    return generateToken(authentication.getName(), getAuthorities(authentication));
  }

  /**
   * JWT 토큰을 생성하고, Redis 에 저장
   *
   * @param email    사용자 이메일
   * @param userType 사용자 타입
   * @return 생성된 토큰 정보를 담은 TokenDto 객체
   */
  private TokenDto generateToken(String email, String userType) {
    TokenDto tokenDto = tokenProvider.generateToken(email, userType);

    // 생성된 토큰 정보를 Redis 에 저장
    redisService.setDataExpire(REFRESH_TOKEN_PREFIX + tokenDto.getAccessToken(),
        tokenDto.getRefreshToken(), tokenDto.getRefreshTokenExpireTime());

    return tokenDto;
  }

  /**
   * 인증 객체에서 권한 정보를 반환
   *
   * @param authentication 인증 객체
   * @return 인증 객체에서 가져온 권한 정보를 문자열로 변환하여 반환합니다.
   */
  private String getAuthorities(Authentication authentication) {
    return authentication.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.joining());
  }
}
