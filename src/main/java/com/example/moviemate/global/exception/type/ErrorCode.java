package com.example.moviemate.global.exception.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

  /**
   * 400 Bad Request
   */
  // Common error
  INVALID_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),

  // User error
  PASSWORD_NOT_MATCH(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다"),
  INVALID_AUTH_CODE(HttpStatus.BAD_REQUEST, "인증번호가 일치하지 않습니다."),

  /**
   * 401 Unauthorized
   */
  USER_AUTHORITY_NOT_MATCH(HttpStatus.UNAUTHORIZED, "사용자 권한이 없습니다."),

  /**
   * 404 Not Found
   */

  USER_NOT_FOUND(HttpStatus.NOT_FOUND, "이메일에 해당되는 사용자가 없습니다."),

  /**
   * 409 conflict
   */

  // User error
  DUPLICATE_USER(HttpStatus.CONFLICT, "이미 존재하는 회원입니다."),

  /**
   * 500 Internal Server Error
   */
  INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버에 오류가 발생했습니다."),
  ;

  private final HttpStatus httpStatus;
  private final String description;
}