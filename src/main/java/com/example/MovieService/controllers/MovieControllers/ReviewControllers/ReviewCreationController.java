package com.example.MovieService.controllers.MovieControllers.ReviewControllers;

import com.example.MovieService.models.Movie;
import com.example.MovieService.models.Review;
import com.example.MovieService.models.User;
import com.example.MovieService.models.dtos.reviewDtos.ReviewDto;
import com.example.MovieService.sevices.MovieServiceImpl;
import com.example.MovieService.sevices.ReviewServiceImpl;
import com.example.MovieService.sevices.UserServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "ReviewCreationController API")
@RestController
@Slf4j
@RequestMapping("/reviews")
public class ReviewCreationController {
    private final MovieServiceImpl movieServiceImpl;
    private final UserServiceImpl userServiceImpl;
    private final ReviewServiceImpl reviewServiceImpl;

    @Autowired
    public ReviewCreationController(MovieServiceImpl movieServiceImpl, UserServiceImpl userServiceImpl, ReviewServiceImpl reviewServiceImpl) {
        this.movieServiceImpl = movieServiceImpl;
        this.userServiceImpl = userServiceImpl;
        this.reviewServiceImpl = reviewServiceImpl;
    }

    @ApiOperation("Get all reviews")
    @GetMapping
    public ResponseEntity<List<ReviewDto>> getReviews(){
        return ResponseEntity.ok(reviewServiceImpl.findAllReviews());
    }

    @ApiOperation("Get review by id")
    @GetMapping("/{id}")
    public ResponseEntity<ReviewDto> getReview(@PathVariable("id") long id) {
        return ResponseEntity.ok(reviewServiceImpl.findReviewDtoById(id));
    }

    @ApiOperation("Create a review")
    @PostMapping("/create/{movieId}")
    public ResponseEntity<Review> createReview(@PathVariable Long movieId, @NotBlank @RequestBody String reviewText) {
        log.info("Creating review for movie with ID: " + movieId);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userServiceImpl.findByUsername(authentication.getName());

        Movie movie = movieServiceImpl.findMovieById(movieId);

        Review review = new Review();
        review.setUser(currentUser);
        review.setMovie(movie);
        review.setReviewText(reviewText);

        currentUser.getReviews().add(review);
        userServiceImpl.save(currentUser);

        log.info("Review created successfully for movie with ID: " + movieId);
        return ResponseEntity.ok(review);
    }

    @ApiOperation("Delete a review")
    @DeleteMapping("/delete/{reviewId}")
    public ResponseEntity<String> deleteReview(@PathVariable Long reviewId) {
        log.info("Deleting review with ID: " + reviewId);

        Review review = reviewServiceImpl.findReviewById(reviewId);

        // Authorization check
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userServiceImpl.findByUsername(authentication.getName());
        if ((!currentUser.hasRole("ROLE_ADMIN") && !review.getUser().equals(currentUser))) {
            log.error("Unauthorized deletion attempt for review with ID: " + reviewId);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized to delete this review.");
        }

        // Remove the review from the movie's review list
        Movie movie = review.getMovie();
        movie.getReviews().remove(review);
        movieServiceImpl.saveMovie(movie);

        // Remove the review from the user's review list
        User user = review.getUser();
        user.getReviews().remove(review);
        userServiceImpl.save(user);

        reviewServiceImpl.deleteReview(review);

        log.info("Review deleted successfully with ID: " + reviewId);
        return ResponseEntity.ok("Review deleted successfully.");
    }

    @ApiOperation("Update a review")
    @PutMapping("/edit/{reviewId}")
    public ResponseEntity<String> editReview(@PathVariable Long reviewId, @NotBlank @RequestBody String updatedReviewText) {
        log.info("Editing review with ID: " + reviewId);
        Review review = reviewServiceImpl.findReviewById(reviewId);

        // Authorization check
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userServiceImpl.findByUsername(authentication.getName());
        if (!review.getUser().equals(currentUser)) {
            log.error("Unauthorized edit attempt for review with ID: " + reviewId);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized to edit this review.");
        }

        // Update the review text
        review.setReviewText(updatedReviewText);
        reviewServiceImpl.saveReview(review);

        log.info("Review edited successfully with ID: " + reviewId);
        return ResponseEntity.ok("Review updated successfully");
    }

    @ApiOperation("Create a reply.")
    @PostMapping("/create/{movieId}/{reviewId}")
    public ResponseEntity<Review> createReply(@PathVariable Long movieId, @PathVariable Long reviewId, @RequestBody String reviewText) {
        log.info("Creating review for movie with ID: " + movieId);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userServiceImpl.findByUsername(authentication.getName());

        Review reply = new Review();
        reply.setUser(currentUser);
        reply.setParent(reviewServiceImpl.findReviewById(reviewId));
        reply.setReviewText(reviewText);
        reply.setMovie(movieServiceImpl.findMovieById(movieId));

        reviewServiceImpl.saveReview(reply);

        log.info("Review created successfully for movie with ID: " + movieId);
        return ResponseEntity.ok(reply);
    }
}