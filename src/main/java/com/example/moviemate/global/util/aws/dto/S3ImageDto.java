package com.example.moviemate.global.util.aws.dto;

import com.example.moviemate.global.util.aws.entity.Image;

public record S3ImageDto(
    String url,
    String fileName,
    Long size
) {
  public Image toEntity() {
    return Image.builder()
        .url(url)
        .fileName(fileName)
        .size(size)
        .build();
  }
}