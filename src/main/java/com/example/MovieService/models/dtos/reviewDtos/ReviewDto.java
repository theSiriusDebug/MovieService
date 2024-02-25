package com.example.MovieService.models.dtos.reviewDtos;

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
    private List<ReviewDto>replies;
}
