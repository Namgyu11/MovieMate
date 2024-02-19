package com.example.moviemate.comment.entity;
import com.example.moviemate.post.entity.Post;
import com.example.moviemate.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.*;
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String content;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "post_id")
  private Post post;

  @Column(nullable = false)
  private int likeCount;

  @OneToMany(mappedBy = "comment", fetch = FetchType.LAZY, orphanRemoval = true)
  private List<CommentLike> commentLikes;

  public void addPostAndMember(Post post, User user) {
    this.post = post;
    this.user = user;

    post.addComment(this);
  }

  public void changeContent(String content) {
    this.content = content;
  }

  public void addCommentLike(CommentLike commentLike) {
    this.commentLikes.add(commentLike);
  }

  public void updateLikeCount() {
    this.likeCount = this.commentLikes.size();
  }

  public void removeCommentLike(CommentLike commentLike) {
    this.commentLikes.remove(commentLike);
  }
}
