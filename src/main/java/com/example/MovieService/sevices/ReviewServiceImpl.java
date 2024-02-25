package com.example.MovieService.sevices;

import com.example.MovieService.models.Review;
import com.example.MovieService.models.dtos.reviewDtos.ReviewDto;
import com.example.MovieService.repositories.ReviewRepository;
import com.example.MovieService.sevices.interfaces.ReviewService;
import com.example.MovieService.utils.mappers.review.ReviewMapper;
import jakarta.validation.Valid;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository repository;

    @Autowired
    public ReviewServiceImpl(ReviewRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<ReviewDto> findAllReviews() {
        log.info("Return all replies");
        return repository.findAll().stream()
                .map(ReviewMapper::mapToReviewDto)
                .collect(Collectors.toList());
    }

    @Override
    public Review findReviewById(long id) {
        try {
            log.info("Return review with id {}", id);
            return repository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Review not found with ID: " + id));
        } catch (NotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ReviewDto findReviewDtoById(long id) {
        try {
            log.info("Return review with id {}", id);
            return ReviewMapper.mapToReviewDto(repository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Review not found with ID: " + id)));
        } catch (NotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteReview(@Valid Review review) {
        log.info("Deleting review with ID: {}", review.getId());
        repository.delete(Objects.requireNonNull(
                review, "Review cannot be null."));
    }

    @Override
    public void saveReview(@Valid Review review) {
        repository.save(Objects.requireNonNull(review, "Review cannot be null."));
        log.info("Review saved successfully.");
    }
}
