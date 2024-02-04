package com.example.moviemate.global.util.jwt;


import static com.example.moviemate.global.exception.type.ErrorCode.EXPIRED_TOKEN;
import static com.example.moviemate.global.exception.type.ErrorCode.INVALID_TOKEN;
import static com.example.moviemate.global.exception.type.ErrorCode.UNSUPPORTED_TOKEN;
import static com.example.moviemate.global.exception.type.ErrorCode.WRONG_TYPE_TOKEN;

import com.example.moviemate.global.exception.GlobalException;
import com.example.moviemate.global.exception.type.ErrorCode;
import com.example.moviemate.global.util.jwt.dto.TokenDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;


@Slf4j
@Component
@RequiredArgsConstructor
public class TokenProvider {

  @Value("${spring.jwt.secret}")
  private String secretKey;

  @Value("${spring.jwt.access-token-expire-time}")
  private Long accessTokenExpiration;

  @Value("${spring.jwt.refresh-token-expire-time}")
  private Long refreshTokenExpiration;

  private static final String KEY_ROLES = "roles";
  private static final String USER_ID = "userId";
  
  private final JwtUserDetailService userDetailService;

  public TokenDto generateToken(String email, String userType) {
    Date now = new Date();

    Date accessTokenExpireTime = new Date(now.getTime() + accessTokenExpiration);
    Date refreshTokenExpireTime = new Date(now.getTime() + refreshTokenExpiration);

    String accessToken = Jwts.builder()
        .setSubject("access-token")
        .claim(KEY_ROLES, userType)
        .claim(USER_ID, email)
        .setIssuedAt(now) // 토큰 생성 시간
        .setExpiration(accessTokenExpireTime) // 토큰 만료 시간
        .signWith(getSigningKey(), SignatureAlgorithm.HS256) // 사용할 암호화 알고리즘 비밀키
        .compact();

    String refreshToken = Jwts.builder()
        .setSubject("refresh-token")
        .claim(KEY_ROLES, userType)
        .claim(USER_ID, email)
        .setIssuedAt(now) // 토큰 생성 시간
        .setExpiration(refreshTokenExpireTime) // 토큰 만료 시간
        .signWith(getSigningKey(), SignatureAlgorithm.HS256) // 사용할 암호화 알고리즘, 비밀키
        .compact();

    return TokenDto.builder().build();
  }

  public boolean validateToken(String token){
    try{
      Claims claims = this.parseClaims(token);
      return !claims.getExpiration().before(new Date()); // 토큰의 만료 시간이 현재의 시간보다 이전인지 아닌지 확인
    }catch (SecurityException | MalformedJwtException e){
      log.error("Invalid Jwt token: {}", e.getMessage());
      throw new GlobalException(INVALID_TOKEN);
    }catch (ExpiredJwtException e){
      log.error("Jwt token is expired: {}", e.getMessage());
      throw new GlobalException(EXPIRED_TOKEN);
    }catch (UnsupportedJwtException e){
      log.error("Jwt token is unsupported: {}", e.getMessage());
      throw new GlobalException(UNSUPPORTED_TOKEN);
    }catch (IllegalArgumentException e){
      log.error("Jwt token is wrong type: {}", e.getMessage());
      throw new GlobalException(WRONG_TYPE_TOKEN);
    }
  }

  public Authentication getAuthentication(String token){
    Claims claims = this.parseClaims(token);
    String userId = claims.get(USER_ID).toString();

    JwtUserDetails userDetails = (JwtUserDetails) userDetailService.loadUserByUsername(userId);

    return new UsernamePasswordAuthenticationToken(userDetails, "",
        userDetails.getAuthorities());
  }

  /**
   * 토큰으로부터 Claim 정보를 가져옴.
   * @param token
   * @return
   */
  private Claims parseClaims(String token){
    return Jwts.parserBuilder()
        .setSigningKey(getSigningKey())
        .build()
        .parseClaimsJws(token)
        .getBody();
  }

  private Key getSigningKey(){
    String encoded = Base64.getEncoder().encodeToString(
        secretKey.getBytes(StandardCharsets.UTF_8));

    return Keys.hmacShaKeyFor(encoded.getBytes());

  }

}
