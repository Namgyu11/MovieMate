package com.example.moviemate.movie.repository;

import com.example.moviemate.movie.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<Movie, String>{

  boolean existsById(String id);


}
