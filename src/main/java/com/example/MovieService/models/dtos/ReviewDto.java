package com.example.MovieService.models.dtos;

import com.example.MovieService.models.Review;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDto {
    private String reviewText;
    private String reviewOwner;
    private List<Review>replies;
}