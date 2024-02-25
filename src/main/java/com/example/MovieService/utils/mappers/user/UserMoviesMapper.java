package com.example.MovieService.utils.mappers.user;

import com.example.MovieService.models.Movie;
import com.example.MovieService.models.dtos.userDtos.UserMoviesDto;

public class UserMoviesMapper {
    public static UserMoviesDto mapToUserMovieDto(Movie movie){
        return new UserMoviesDto(
                movie.getTitle(),
                movie.getImdbRating(),
                movie.getKinopoiskRating(),
                movie.getPosterLink()
        );
    }
}
