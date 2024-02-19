package com.example.moviemate.movie.repository;

import com.example.moviemate.movie.entity.People;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PeopleRepository extends JpaRepository<People, String> {

  boolean existsById(String id);

  @Query("select distinct p.id from People p")
  List<String> findByAllId();
}
