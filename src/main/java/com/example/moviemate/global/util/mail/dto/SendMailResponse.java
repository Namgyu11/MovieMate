package com.example.moviemate.global.util.mail.dto;

import java.util.concurrent.Future;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SendMailResponse {
  private String email;
  private String code;
}
