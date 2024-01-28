package com.example.moviemate.auth.dto;



import com.example.moviemate.user.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpDto {

  @Email
  private String email;

  @NotBlank
  private String password;

  @NotBlank
  @Size(max = 10)
  private String nickname;

  @NotBlank
  private String phoneNumber;

  public static SignUpDto fromEntity(User user){
    return SignUpDto.builder()
        .email(user.getEmail())
        .password(user.getPassword())
        .nickname(user.getNickname())
        .phoneNumber(user.getPhoneNumber())
        .build();
  }

}
