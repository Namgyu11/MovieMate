package com.example.moviemate.post.repository;


import com.example.moviemate.post.entity.Post;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, CustomPostRepository{

  @Query("select p from Post p where p.content like concat('%', :content, '%')"
  +"order by p.id desc, p.views desc, p.likeCount desc")
  Page<Post> findAllByContentContaining(String content, Pageable pageable);

  @Query("select p from Post p left join p.postCategory c where  c.id = :categoryId")
  Page<Post> findPostsByCategoryId(@Param("categoryId") Long categoryId, Pageable pageable);

}
