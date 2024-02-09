package com.example.moviemate.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserDto {

  @NotBlank
  private String password;
  @NotBlank
  private String nickname;
  @NotBlank
  private String phoneNumber;

}
