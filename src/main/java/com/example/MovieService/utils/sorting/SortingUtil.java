package com.example.MovieService.utils.sorting;

import org.springframework.data.domain.Sort;

import java.util.Map;

public class SortingUtil {
    public static Sort getSorting(String sortType) { // Helper method to get the sorting order from the request parameter
        Map<String, Sort.Order> sortTypes = Map.of(
                "by_date", Sort.Order.desc("year"),
                "by_date_reverse", Sort.Order.asc("year"),
                "by_alphabet", Sort.Order.desc("title"),
                "by_alphabet_reverse", Sort.Order.asc("title"),
                "by_rating", Sort.Order.desc("imdbRating"),
                "by_rating_reverse", Sort.Order.asc("imdbRating"),
                "by_kinopoisk_rating", Sort.Order.desc("kinopoiskRating"),
                "by_kinopoisk_rating_reverse", Sort.Order.asc("kinopoiskRating")
        );

        // Get the sorting order from the request parameter
        Sort.Order order = sortTypes.getOrDefault(sortType, Sort.Order.desc("year"));

        return Sort.by(order); // Create a Sort object with the specified order
    }
}
