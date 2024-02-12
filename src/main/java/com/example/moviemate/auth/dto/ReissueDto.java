package com.example.moviemate.auth.dto;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReissueDto {

  @NotBlank(message = "Access token 은 필수 입력 항목입니다.")
  private String accessToken;

  @NotBlank(message = "Refresh token 은 필수 입력 항목입니다.")
  private String refreshToken;
}
