package com.example.moviemate.global.util.jwt.dto;
import com.example.moviemate.user.entity.User;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomUserDto {

  private Long id;
  private String email;
  private String password;
  private String userType;

  public static CustomUserDto fromEntity(User user){
    return CustomUserDto.builder()
        .id(user.getId())
        .email(user.getEmail())
        .password(user.getPassword())
        .userType(user.getUserType().getCode())
        .build();
  }

}
