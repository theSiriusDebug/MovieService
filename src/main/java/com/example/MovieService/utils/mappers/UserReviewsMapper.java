package com.example.MovieService.utils.mappers;

import com.example.MovieService.models.Review;
import com.example.MovieService.models.dtos.UserReviewsDto;

public class UserReviewsMapper {
    public static UserReviewsDto mapToUserReviewsDto(Review review){
        return new UserReviewsDto(
                review.getReviewText()
        );
    }
}