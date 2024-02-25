package com.example.MovieService.utils.mappers.user;

import com.example.MovieService.models.User;
import com.example.MovieService.models.dtos.userDtos.UserDto;

import java.util.stream.Collectors;

public class UserMapper {
    public static UserDto mapToUserDto(User user){
        return new UserDto(
                user.getUsername(),
                user.getRoles(),
                user.getFavoriteMovies().stream().map(UserMoviesMapper::mapToUserMovieDto).collect(Collectors.toList()),
                user.getWatchLaterMovies().stream().map(UserMoviesMapper::mapToUserMovieDto).collect(Collectors.toList()),
                user.getViewedMovies().stream().map(UserMoviesMapper::mapToUserMovieDto).collect(Collectors.toList()),
                user.getReviews().stream()
                        .map(UserReviewsMapper::mapToUserReviewsDto)
                        .collect(Collectors.toList())
        );
    }
}
