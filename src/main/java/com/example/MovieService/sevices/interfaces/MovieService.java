package com.example.MovieService.sevices.interfaces;

import com.example.MovieService.models.Movie;
import com.example.MovieService.models.dtos.movieDtos.MovieDto;
import com.example.MovieService.models.dtos.movieDtos.MovieFilterDTO;

import java.util.List;

public interface MovieService {
    List<MovieDto> getFilteredMovies(MovieFilterDTO dto);

    List<MovieDto> getMovies(String sortType);

    Movie findMovieById(long id);

    void saveMovie(Movie movie);
}
