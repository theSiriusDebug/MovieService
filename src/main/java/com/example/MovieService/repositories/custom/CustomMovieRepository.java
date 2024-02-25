package com.example.MovieService.repositories.custom;

import com.example.MovieService.models.dtos.movieDtos.MovieDto;
import com.example.MovieService.models.dtos.movieDtos.MovieFilterDTO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomMovieRepository {
    List<MovieDto> findMovies(MovieFilterDTO filterDto);
}
