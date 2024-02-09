package com.example.moviemate.user.dto;


import com.example.moviemate.user.entity.User;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto{

  private Long id;
  private String email;
  private String nickname;
  private String password;
  private String phoneNumber;
  private String roleType;
  private boolean emailAuth;

  public static UserDto fromEntity(User user) {
    return UserDto.builder()
        .id(user.getId())
        .email(user.getEmail())
        .nickname(user.getNickname())
        .password(user.getPassword())
        .roleType(user.getUserType().getCode())
        .phoneNumber(user.getPhoneNumber())
        .emailAuth(user.isEmailAuth())
        .build();
  }


}
