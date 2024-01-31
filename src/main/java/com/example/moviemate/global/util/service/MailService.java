package com.example.moviemate.global.util.service;

import static com.example.moviemate.global.exception.type.ErrorCode.*;

import com.example.moviemate.global.exception.GlobalException;
import com.example.moviemate.global.service.RedisService;
import com.example.moviemate.global.util.dto.SendMailResponse;
import com.example.moviemate.user.entity.User;
import com.example.moviemate.user.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {

  private final JavaMailSender javaMailSender;
  private final RedisService redisService;
  private final UserRepository userRepository;

  private static final int CODE_LENGTH = 6;
  private static final Long EMAIL_TOKEN_EXPIRATION = 600000L;
  private static final String EMAIL_PREFIX = "Email-Auth";

  public SendMailResponse sendAuthMail(String email) {
    String code = createRandomCode();

    MimeMessage mimeMailMessage = javaMailSender.createMimeMessage();
    MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMailMessage, "utf-8");

   try{
     mimeMessageHelper.setTo(email); // 메일 수신자
     mimeMailMessage.setSubject("== 회원가입을 위한 이메일 인증코드 ==");

     String msgCode = "<div style='margin:20px;'>"
         + "<h1> 안녕하세요 'MovieMate' 입니다. </h1>"
         + "<br>"
         + "<p>아래 코드를 입력해주세요<p>"
         + "<br>"
         + "<p>감사합니다.<p>"
         + "<br>"
         + "<div align='center' style='border:1px solid black; font-family:verdana';>"
         + "<h3 style='color:blue;'>회원가입 인증 코드입니다.</h3>"
         + "<div style='font-size:130%'>"
         + "CODE : <strong>" + code + "</strong><div><br/> "
         + "</div>";

     mimeMessageHelper.setText(msgCode, true); // 메일 본문 내용, HTML 여부

   }catch (MessagingException e){
     log.info("Mail sand fail");
     throw new GlobalException(INTERNAL_SERVER_ERROR);
   }
    javaMailSender.send(mimeMailMessage);

    redisService.setDataExpire(EMAIL_PREFIX + email, code, EMAIL_TOKEN_EXPIRATION);

    return SendMailResponse.builder()
        .email(email)
        .code(code)
        .build();
  }

  @Transactional
  public void verifyEmail(String email, String code){
    if(!isVerify(email, code)){
      throw new GlobalException(INVALID_AUTH_CODE);
    }

    // User 이메일 인증 여부 변경
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new GlobalException(USER_NOT_FOUND));
    user.changeEmailAuth();
    userRepository.save(user);

    redisService.deleteData(email);
  }
  private boolean isVerify(String email, String code){
    String data = redisService.getData(EMAIL_PREFIX + email);
    if(data == null){
      throw new GlobalException(USER_NOT_FOUND);
    }

    return data.equals(code);
  }
  private String createRandomCode() {
    Random random = new Random();
    StringBuilder builder = new StringBuilder();

    while (builder.length() < CODE_LENGTH) {
      builder.append(random.nextInt(10));
    }

    return builder.toString();
  }

}
