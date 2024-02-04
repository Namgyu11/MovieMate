package com.example.moviemate.global.util.jwt;

import static com.example.moviemate.global.exception.type.ErrorCode.UNKNOWN_ERROR;

import com.example.moviemate.global.exception.GlobalException;
import com.example.moviemate.global.service.RedisService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final TokenProvider tokenProvider;
  private final RedisService redisService;
  @Value("${spring.jwt.prefix}")
  private String tokenPrefix;

  @Value("${spring.jwt.header}")
  private String tokenHeader;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    String token = resolveTokenFromRequest(request);

    try {
      if (StringUtils.hasText(token) && tokenProvider.validateToken(token)) {

        if (Objects.isNull(redisService.getData(token))) {
          Authentication authentication = tokenProvider.getAuthentication(token);
          SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
          request.setAttribute("exception", UNKNOWN_ERROR);
        }
      }
    } catch (GlobalException e) {
      request.setAttribute("exception", UNKNOWN_ERROR);
    }

    filterChain.doFilter(request, response);
  }

  private String resolveTokenFromRequest(HttpServletRequest request) {
    String token = request.getHeader(this.tokenHeader);
    if (!ObjectUtils.isEmpty(token) && token.startsWith(this.tokenPrefix)) {
      return token.substring(this.tokenPrefix.length());
    }
    return null;
  }
}
