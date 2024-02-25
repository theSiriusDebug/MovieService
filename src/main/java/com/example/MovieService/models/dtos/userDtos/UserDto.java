package com.example.MovieService.models.dtos.userDtos;

import com.example.MovieService.models.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String username;
    private Set<Role> roles;
    private List<UserMoviesDto> favoriteMovies;
    private List<UserMoviesDto> watchLaterMovies;
    private List<UserMoviesDto> viewedMovies;
    private List<UserReviewsDto> reviews;
}
