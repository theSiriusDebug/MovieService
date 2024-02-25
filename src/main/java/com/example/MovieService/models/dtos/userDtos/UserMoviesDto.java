package com.example.MovieService.models.dtos.userDtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserMoviesDto {
    private String title;
    private double imdbRating;
    private double kinopoiskRating;
    private String posterLink;
}
