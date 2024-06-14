package com.example.moviemate.movie.config;

import com.example.moviemate.movie.dto.api.movieDetails.MovieDetailInfoApiResponse;
import com.example.moviemate.movie.dto.api.movieList.MovieListApiResponse;
import com.example.moviemate.movie.dto.api.peopleDetails.PeopleDetailInfoApiResponse;
import com.example.moviemate.movie.dto.api.peopleList.PeopleListApiResponse;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange
public interface MovieApi {

  @GetExchange("/movie/searchMovieList.json")
  MovieListApiResponse getApiSearchMovieListByName(@RequestParam("key") String key,
      @RequestParam("movieNm") String movieNm);

  @GetExchange("/movie/searchMovieInfo.json")
  MovieDetailInfoApiResponse getApiSearchMovieInfoById(@RequestParam("key") String key,
      @RequestParam("movieCd") String movieCd);

  @GetExchange("/people/searchPeopleList.json")
  PeopleListApiResponse getApiSearchPeopleListByName(@RequestParam("key") String key,
      @RequestParam("peopleNm") String peopleNm);

  @GetExchange("/people/searchPeopleInfo.json")
  PeopleDetailInfoApiResponse getApiSearchPeopleInfoById(@RequestParam("key") String key,
      @RequestParam("peopleCd")  String peopleCd);


}

