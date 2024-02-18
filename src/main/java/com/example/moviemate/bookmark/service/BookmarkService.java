package com.example.moviemate.bookmark.service;

import com.example.moviemate.bookmark.dto.BookmarkRequest;
import com.example.moviemate.bookmark.dto.BookmarkResponse;
import java.util.List;

public interface BookmarkService {

  BookmarkResponse createBookmark(String username, BookmarkRequest bookmarkRequest);

  List<BookmarkResponse> findBookmarks(String username);

  void deleteBookmark(String username, Long id);


}
