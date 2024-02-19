package com.example.moviemate.post.service.impl;


import com.example.moviemate.global.exception.GlobalException;
import com.example.moviemate.global.exception.type.ErrorCode;
import com.example.moviemate.post.dto.PostLikeResponse;
import com.example.moviemate.post.entity.Post;
import com.example.moviemate.post.entity.PostLike;
import com.example.moviemate.post.repository.PostLikeRepository;
import com.example.moviemate.post.repository.PostRepository;
import com.example.moviemate.post.service.PostLikeService;
import com.example.moviemate.user.entity.User;
import com.example.moviemate.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostLikeServiceImpl implements PostLikeService {

  private final PostRepository postRepository;
  private final PostLikeRepository postLikeRepository;
  private final UserRepository userRepository;


  @Override
  @Transactional
  public PostLikeResponse likePost(Long postId, String username) {
    if(postLikeRepository.existsPostLikeByPostIdAndUser_Email(postId, username)){
      throw new GlobalException(ErrorCode.ALREADY_LIKED);
    }
    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new GlobalException(ErrorCode.POST_NOT_FOUND));

    User user = userRepository.findByEmail(username)
        .orElseThrow(() -> new GlobalException(ErrorCode.USER_NOT_FOUND));

    PostLike postLike = PostLike.builder().build();

    postLike.addUser(user);
    postLike.addPost(post);
    post.updateLikeCount();

    postLikeRepository.save(postLike);

    return PostLikeResponse.builder()
        .postId(postId)
        .username(user.getEmail())
        .isLike(true)
        .build();
  }

  @Override
  @Transactional
  public PostLikeResponse unlikePost(Long postId, String username) {

    return postLikeRepository.findByPostIdAndUser_Email(postId, username)
        .map(postLike ->{
          postLikeRepository.delete(postLike);
          postRepository.findById(postId).ifPresent(e -> {
            e.removePostLike(postLike);
            e.updateLikeCount();
          });

          return PostLikeResponse.builder()
              .postId(postId)
              .username(username)
              .isLike(false)
              .build();
        }).orElseThrow(() -> new GlobalException(ErrorCode.ALREADY_UNLIKED));
  }
}
