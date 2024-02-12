package com.example.moviemate.user.dto;

import com.example.moviemate.user.entity.User;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserResponse {
  private String changePasswordMessage;
  private String nickname;
  private String phoneNumber;

  public static UpdateUserResponse fromEntity(User user) {
    return UpdateUserResponse.builder()
        .changePasswordMessage("비밀번호가 변경되었습니다.")
        .nickname(user.getNickname())
        .phoneNumber(user.getPhoneNumber())
        .build();
  }

}
