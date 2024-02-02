package com.example.moviemate.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis DB 연결 설정
 */
@Configuration
public class RedisConfig {
  @Value("${spring.data.redis.host}")
  private String host;

  @Value("${spring.data.redis.port}")
  private int port;

  @Bean
  public RedisConnectionFactory redisConnectionFactory(){
    return new LettuceConnectionFactory(host, port);
  }

  @Bean
  public RedisTemplate<String, Object> redisTemplate(){
    RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();

    // RedisTemplate 에 위에서 생성한 Redis 연결 팩토리를 설정
    redisTemplate.setConnectionFactory(redisConnectionFactory());

    // RedisTemplate 에 키와 값을 String 형태로 직렬화/역직렬화
    redisTemplate.setKeySerializer(new StringRedisSerializer());
    redisTemplate.setValueSerializer(new StringRedisSerializer());

    // RedisTemplate 에 해시 키와 값을 String 형태로 직렬화/역직렬화
    redisTemplate.setHashKeySerializer(new StringRedisSerializer());
    redisTemplate.setHashValueSerializer(new StringRedisSerializer());

    // RedisTemplate 에 기본 직렬화 도구로 설정
    redisTemplate.setDefaultSerializer(new StringRedisSerializer());

    return redisTemplate;
  }
}
