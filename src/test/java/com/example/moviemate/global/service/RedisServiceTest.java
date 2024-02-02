package com.example.moviemate.global.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class RedisServiceTest {

  @Autowired
  private RedisService redisService;

  @Test
  @DisplayName("메일 인증코드 테스트")
  void sendMail() {
    //given
    String email = "test@test1.com";
    String code = "12345";
    Long expiredTime = 60 * 60L;

    //when
    redisService.setDataExpire(email, code, expiredTime);

    //then
    Assertions.assertTrue(redisService.existData("test@test1.com"));
    Assertions.assertFalse(redisService.existData("test12@test.com"));
    Assertions.assertEquals(redisService.getData(email), code);
  }
}