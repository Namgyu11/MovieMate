package com.example.moviemate.global.util.jwt.dto;


import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenDto {

  private String accessToken;
  private String refreshToken;
  private Long accessTokenExpireTime;
  private Long refreshTokenExpireTime;

}
