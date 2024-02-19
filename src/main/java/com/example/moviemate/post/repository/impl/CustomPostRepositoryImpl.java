package com.example.moviemate.post.repository.impl;

import static com.example.moviemate.post.entity.QPost.post;

import com.example.moviemate.post.entity.Post;
import com.example.moviemate.post.repository.CustomPostRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

@RequiredArgsConstructor
public class CustomPostRepositoryImpl implements CustomPostRepository {

  private final JPAQueryFactory jpa;
  private final EntityManager entityManager;

  @Override
  public Slice<Post> searchByTitle(Long postId, String title, Pageable pageable) {
    List<Post> postList = jpa.selectFrom(post)
        .where(
            ltPostId(postId)
            , post.title.contains(title)
        )
        .orderBy(
            post.id.desc(),
            post.views.desc(),
            post.likeCount.desc()
        )
        .limit(pageable.getPageSize() + 1)
        .fetch();

    return checkLastPage(pageable, postList);
  }

  @Override
  public void updateViews(Long id, int views) {
    jpa.update(post)
        .set(post.views, post.views.add(views))
        .where(post.id.eq(id))
        .execute();
    entityManager.flush();
    entityManager.clear();
  }

  private BooleanExpression ltPostId(Long postId) {
    if (postId == null || postId == 0L) {
      return null;
    }

    return post.id.lt(postId);
  }


  private Slice<Post> checkLastPage(Pageable pageable, List<Post> results) {

    boolean hasNext = false;

    if (results.size() > pageable.getPageSize()) {
      hasNext = true;
      results.remove(pageable.getPageSize());
    }

    return new SliceImpl<>(results, pageable, hasNext);
  }

}
