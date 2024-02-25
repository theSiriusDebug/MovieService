package com.example.MovieService.models.dtos.movieDtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieFilterDTO {
    private String title;
    private Integer maxYear;
    private Integer minYear;
    private Double maxImdbRating;
    private Double minImdbRating;
    private Double maxKinopoiskRating;
    private Double minKinopoiskRating;
    private String durationMax;
    private String durationMin;
    private String country;
    private String director;
    private String genre;
    private String language;
    private String sortCriteria;
}
