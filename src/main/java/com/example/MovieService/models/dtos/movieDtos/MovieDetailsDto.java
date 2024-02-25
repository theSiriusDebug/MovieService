package com.example.MovieService.models.dtos.movieDtos;

import com.example.MovieService.models.dtos.reviewDtos.ReviewDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieDetailsDto {
    private String title;
    private int year;
    private String quality;
    private String language;
    private String duration;
    private String country;
    @JsonIgnore
    private List<String> genres;
    private double imdbRating;
    private double kinopoiskRating;
    private String director;
    private String posterLink;
    private String trailerLink;
    private String movieLink;
    @JsonIgnore
    private List<String> actors;
    private List<ReviewDto> reviews;
}
