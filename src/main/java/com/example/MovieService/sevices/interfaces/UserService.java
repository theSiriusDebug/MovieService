package com.example.MovieService.sevices.interfaces;

import com.example.MovieService.models.Movie;
import com.example.MovieService.models.User;
import com.example.MovieService.models.dtos.UserDto;
import com.example.MovieService.models.dtos.userDtos.EditUserDto;

import java.util.List;

public interface UserService {
    void removeFromList(User user, long movieId, List<Movie> movies);
    User addMovieToList(User user, long movieId, List<Movie> movies);

    User updateUser(EditUserDto editUser, User user);

    User findByOptionalUsername(String username);

    boolean userExists(String username);

    User save(User user);

    List<UserDto> findAllUsers();

    UserDto findUserById(Long id);
}
