package com.example.moviemate.post.service;

import com.example.moviemate.post.dto.PostRequest;
import com.example.moviemate.post.dto.PostResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.web.multipart.MultipartFile;

public interface PostService {

  /**
   *  게시물 생성
   */
  PostResponse createPost(PostRequest requestDto, String username, List<MultipartFile> files);

  /**
   * 게시물 조회
   */
  PostResponse readPost(Long id, HttpServletRequest request);

  /**
   * 게시물 수정
   */
  PostResponse updatePost(Long id, PostRequest requestDto, String username);


  /**
   * 게시물 삭제
   */

  void deletePost(Long id, String username);

  /**
   *  단일 게시물 검색
   */
  PostResponse findPost(Long id);

  /**
   *  게시물 목록 조회
   */

  Page<PostResponse> findPosts(Pageable pageable);

  /**
   *  제목 - 게시물 검색
   */
  Slice<PostResponse> searchTitle(Long id, String name, Pageable pageable);

  /**
   * 내용 - 게시물 검색
   */
  Page<PostResponse> searchContent(String name, Pageable pageable);


}
