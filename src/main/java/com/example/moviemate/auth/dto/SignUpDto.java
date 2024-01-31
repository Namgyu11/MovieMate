package com.example.moviemate.auth.dto;



import com.example.moviemate.user.entity.User;
import com.example.moviemate.user.entity.type.UserType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

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

  public static User signUpForm(SignUpDto request, PasswordEncoder passwordEncoder){
    return User.builder()
        .email(request.getEmail())
        .password(passwordEncoder.encode(request.getPassword()))
        .nickname(request.getNickname())
        .phoneNumber(request.getPhoneNumber())
        .userType(UserType.USER)
        .build();
  }

}
