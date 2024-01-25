package com.example.MovieService.utils.validator;

import com.example.MovieService.models.Movie;
import com.example.MovieService.models.Rating;
import com.example.MovieService.models.dtos.MovieDto;
import java.util.List;
import java.util.stream.Collectors;

public class MovieMapper {
    public static MovieDto mapToMovieDto(Movie movie){
        return new MovieDto(
                movie.getTitle(),
                movie.getYear(),
                movie.getQuality(),
                movie.getLanguage(),
                movie.getDuration(),
                movie.getCountry(),
                movie.getGenres(),
                movie.getImdbRating(),
                movie.getKinopoiskRating(),
                movie.getPosterLink(),
                movie.getReviews().size(),
                getApproximateAverage(movie.getRatings().stream().map(Rating::getRatingValue).collect(Collectors.toList()))
        );
    }

    public static double getApproximateAverage(List<Integer> list) {
        if (list == null || list.isEmpty()) {
            return 0.0;
        }

        double sum = 0.0;
        for (Integer number : list) {
            sum += number;
        }

        return sum / list.size();
    }
}
