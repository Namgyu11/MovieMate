package com.example.moviemate.global.util.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SendMailRequest {

  @NotBlank(message = "이메일을 입력해주세요.")
  @Email(message = "형식에 맞지 않는 이메일입니다.")
  private String email;
}
