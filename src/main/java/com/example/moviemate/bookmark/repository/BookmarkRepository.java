package com.example.moviemate.bookmark.repository;

import com.example.moviemate.bookmark.entity.Bookmark;
import com.example.moviemate.user.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookmarkRepository  extends JpaRepository<Bookmark, Long>{

  boolean existsByMovieId(String movieCd);

  List<Bookmark> findAllByUser(User user);
}
