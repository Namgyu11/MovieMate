package com.example.moviemate.post.entity;


import com.example.moviemate.comment.entity.Comment;
import com.example.moviemate.global.entity.BaseEntity;
import com.example.moviemate.global.util.aws.entity.Image;
import com.example.moviemate.user.entity.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.*;
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Post extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  private String content;

  @Column(nullable = false)
  private int views;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @Column(nullable = false)
  private int likeCount;

  @Builder.Default
  @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<PostLike> postLikeList = new ArrayList<>();

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "category_id")
  private PostCategory postCategory;

  @Builder.Default
  @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Comment> comments = new ArrayList<>();

  @Builder.Default
  @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Image> images = new ArrayList<>();

  public void updateLikeCount(){
    this.likeCount = this.postLikeList.size();
  }

  public void addPostLike(PostLike postLike){
    this.postLikeList.add(postLike);
  }

  public void removePostLike(PostLike postLike){
    this.postLikeList.remove(postLike);
  }

  public void addCategory(PostCategory category){
    this.postCategory = category;
  }

  public void addComment(Comment comment){
    this.comments.add(comment);
  }

  public void addImage(Image image){
    this.images.add(image);
    image.mappingPost(this);
  }

}
