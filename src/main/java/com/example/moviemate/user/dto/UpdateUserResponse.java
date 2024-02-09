package com.example.moviemate.user.dto;

import com.example.moviemate.user.entity.User;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserResponse {

  private String nickname;
  private String phoneNumber;

  public static UpdateUserResponse fromEntity(User user) {
    return UpdateUserResponse.builder()
        .nickname(user.getNickname())
        .phoneNumber(user.getPhoneNumber())
        .build();
  }

}
