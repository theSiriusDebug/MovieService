package com.example.MovieService.utils.mappers;

import com.example.MovieService.models.Review;
import com.example.MovieService.models.dtos.ReviewDto;
import jakarta.validation.Valid;

import java.util.stream.Collectors;

public class ReviewMapper {
    public static ReviewDto mapToReviewDto(@Valid Review review){
        return new ReviewDto(
                review.getReviewText(),
                review.getUser().getUsername(),
                review.getReplies().stream().map(ReviewMapper::mapToReviewDto).collect(Collectors.toList())
        );
    }
}
