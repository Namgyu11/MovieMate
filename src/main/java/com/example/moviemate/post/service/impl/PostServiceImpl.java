package com.example.moviemate.post.service.impl;


import static com.example.moviemate.global.exception.type.ErrorCode.*;

import com.example.moviemate.global.exception.GlobalException;
import com.example.moviemate.global.exception.type.ErrorCode;
import com.example.moviemate.global.service.RedisService;
import com.example.moviemate.global.util.aws.dto.S3ImageDto;
import com.example.moviemate.global.util.aws.entity.Image;
import com.example.moviemate.global.util.aws.service.AwsS3Service;
import com.example.moviemate.post.dto.PostRequest;
import com.example.moviemate.post.dto.PostResponse;
import com.example.moviemate.post.entity.Post;
import com.example.moviemate.post.repository.PostRepository;
import com.example.moviemate.post.repository.impl.PostCategoryRepository;
import com.example.moviemate.post.service.PostService;
import com.example.moviemate.user.entity.User;
import com.example.moviemate.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

  private final PostRepository postRepository;
  private final UserRepository userRepository;
  private final PostCategoryRepository postCategoryRepository;
  private final AwsS3Service awsS3Service;
  private final RedisService redisService;

  private static final String VIEW_HASH_KEY = "post::views";


  @Override
  @Transactional
  public PostResponse createPost(PostRequest requestDto, String username,
      List<MultipartFile> files) {

    User user = getUser(username);
    Post post = uploadS3Image(requestDto, files);

    user.addPost(post);
    postCategoryRepository.findByName(requestDto.getCategory())
        .ifPresentOrElse(post::addCategory,
            () -> {throw new GlobalException(ErrorCode.CATEGORY_NOT_FOUND);});

    return PostResponse.fromEntity(postRepository.save(post));
  }


  @Override
  @Transactional
  public PostResponse readPost(Long id, HttpServletRequest request) {
    Post post =  getPost(id);

    HttpSession session = request.getSession();

    //클라이언트의 세션에서 중복 조회 여부 확인
    Boolean hasRead = (Boolean) session.getAttribute("readPost:" + id);

    if(hasRead == null || !hasRead){

      //중복 조회 여부를 세션에 저장
      session.setAttribute("readPost:" + id, true);

      //조회수 증가
      redisService.increaseHashData(VIEW_HASH_KEY, id.toString());
    }else {
      log.info(" 중복 요청 발생 Redis == readPost ");
    }

    return PostResponse.fromEntity(post);
  }

  private Post getPost(Long id) {

    return postRepository.findById(id)
        .orElseThrow(() -> new GlobalException(POST_NOT_FOUND));
  }

  @Override
  @Transactional
  public PostResponse updatePost(Long id, PostRequest requestDto, String username) {
    Post post = getPost(id);
    User user = getUser(username);

    validationPost(post, user);

    post.setTitle(requestDto.getTitle());
    post.setContent(requestDto.getContent());

    return PostResponse.fromEntity(post);
  }

  @Override
  public void deletePost(Long id, String username) {
    Post post = getPost(id);
    User user = getUser(username);

    validationPost(post, user);

    deleteS3Image(post.getImages());
    user.removePost(post);
    postRepository.delete(post);
  }

  private void deleteS3Image(List<Image> images){
    images.forEach(e -> awsS3Service.deleteFile(e.getFileName()));
  }

  @Override
  @Transactional(readOnly = true)
  public PostResponse findPost(Long id) {

    return PostResponse.fromEntity(getPost(id));
  }

  @Override
  @Transactional(readOnly = true)
  public Page<PostResponse> findPosts(Pageable pageable) {
    return postRepository.findAll(pageable).map(PostResponse::fromEntity);
  }

  @Override
  @Transactional(readOnly = true)
  public Slice<PostResponse> searchTitle(Long id, String name, Pageable pageable) {

    return postRepository.searchByTitle(id, name, pageable).map(PostResponse::fromEntity);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<PostResponse> searchContent(String name, Pageable pageable) {
    return postRepository.findAllByContentContaining(name, pageable).map(PostResponse::fromEntity);
  }

  private User getUser(String username) {
    return userRepository.findByEmail(username)
        .orElseThrow(() -> new GlobalException(USER_NOT_FOUND));
  }

  private void validationPost(Post post, User user){

    if(!post.getUser().getEmail().equals(user.getEmail())){
      throw new GlobalException(WRITE_NOT_YOURSELF);
    }
  }
  private Post uploadS3Image(PostRequest requestDto, List<MultipartFile> multipartFiles) {
    Post post = requestDto.toEntity();

    List<S3ImageDto> list = multipartFiles.stream().map(awsS3Service::uploadFile).toList();
    List<Image> imageList = list.stream().map(S3ImageDto::toEntity).toList();

    imageList.forEach(post::addImage);
    return post;
  }

  @Scheduled(cron = "${spring.scheduler.refresh-time}")
  public void updateViewCountToDB(){
    Map<Object, Object> map = redisService.hasHashKeys(VIEW_HASH_KEY);

    for(Map.Entry<Object, Object> entry : map.entrySet()){
      Long postId = Long.parseLong(entry.getKey().toString());
      int views = Integer.parseInt(entry.getValue().toString());

      postRepository.updateViews(postId, views);

      redisService.deleteHashKey(VIEW_HASH_KEY, postId.toString());
    }
  }
}
