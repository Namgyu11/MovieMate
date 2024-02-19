package com.example.moviemate.bookmark.service;

import com.example.moviemate.bookmark.dto.BookmarkRequest;
import com.example.moviemate.bookmark.dto.BookmarkResponse;
import com.example.moviemate.bookmark.entity.Bookmark;
import com.example.moviemate.bookmark.repository.BookmarkRepository;
import com.example.moviemate.global.exception.GlobalException;
import com.example.moviemate.global.exception.type.ErrorCode;
import com.example.moviemate.movie.entity.Movie;
import com.example.moviemate.movie.repository.MovieRepository;
import com.example.moviemate.user.entity.User;
import com.example.moviemate.user.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookmarkServiceImpl implements BookmarkService {

  private final BookmarkRepository bookmarkRepository;
  private final UserRepository userRepository;
  private final MovieRepository movieRepository;


  @Override
  public BookmarkResponse createBookmark(String username, BookmarkRequest request) {
    if (bookmarkRepository.existsByMovieId(request.getMovieCd())) {
      throw new GlobalException(ErrorCode.ALREADY_REGISTERED);
    }

    User user = userRepository.findByEmail(username)
        .orElseThrow(() -> new GlobalException(ErrorCode.USER_NOT_FOUND));

    Movie movie = movieRepository.findById(request.getMovieCd())
        .orElseThrow(() -> new GlobalException(ErrorCode.MOVIE_NOT_FOUND));

    Bookmark bookmark = Bookmark.builder().build();
    bookmark.addBookmark(user);
    bookmark.addMovie(movie);

    return BookmarkResponse.fromEntity(bookmarkRepository.save(bookmark));
  }

  @Override
  @Transactional(readOnly = true)
  public List<BookmarkResponse> findBookmarks(String username) {
    User user = userRepository.findByEmail(username)
        .orElseThrow(() -> new GlobalException(ErrorCode.USER_NOT_FOUND));

    return bookmarkRepository.findAllByUser(user).stream().map(BookmarkResponse::fromEntity)
        .toList();
  }

  @Override
  @Transactional
  public void deleteBookmark(String username, Long id) {
    Bookmark bookmark = bookmarkRepository.findById(id).orElseThrow(
        () -> new GlobalException(ErrorCode.BOOKMARK_NOT_FOUND));

    bookmarkRepository.delete(bookmark);
  }
}
