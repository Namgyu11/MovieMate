package com.example.moviemate.user.entity.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserType {

  USER("ROLE_USER"),
  ADMIN("ROLE_ADMIN");

  private final String code;

}
