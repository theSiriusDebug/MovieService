package com.example.MovieService.sevices.interfaces;

import com.example.MovieService.models.Movie;
import com.example.MovieService.models.User;
import com.example.MovieService.models.dtos.userDtos.UserDto;
import com.example.MovieService.models.dtos.userDtos.EditUserDto;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface UserService {
    void addToViewedMovies(long id, Authentication auth);
    void removeFromList(User user, long movieId, List<Movie> movies);
    User addMovieToList(User user, long movieId, List<Movie> movies);

    User updateUser(EditUserDto editUser, User user);

    User findByUsername(String username);

    boolean userExists(String username);

    User save(User user);

    List<UserDto> findAllUsers();

    UserDto findUserById(Long id);
}
