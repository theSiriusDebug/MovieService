package com.example.MovieService.controllers.MovieControllers.ReviewControllers;

import com.example.MovieService.models.Movie;
import com.example.MovieService.models.Rating;
import com.example.MovieService.models.User;
import com.example.MovieService.sevices.MovieServiceImpl;
import com.example.MovieService.sevices.UserServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Api(tags = "RatingCreationController API")
@RestController
@RequestMapping("/rating")
public class RatingCreationController {

    private final MovieServiceImpl movieServiceImpl;
    private final UserServiceImpl userServiceImpl;

    @Autowired
    public RatingCreationController(MovieServiceImpl movieServiceImpl, UserServiceImpl userServiceImpl) {
        this.movieServiceImpl = movieServiceImpl;
        this.userServiceImpl = userServiceImpl;
    }

    @ApiOperation("Create a rating")
    @PostMapping("/create/{movieId}")
    public ResponseEntity<String> createRating(@PathVariable Long movieId, @RequestBody Rating rating) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userServiceImpl.findByUsername(authentication.getName());

        Movie movie = movieServiceImpl.findMovieById(movieId);

        for (Rating existingRating : user.getRating()) {
            if (existingRating.getMovie().getId().equals(movieId)) {
                return new ResponseEntity<>("User has already rated this movie.", HttpStatus.BAD_REQUEST);
            }
        }

        rating.setMovie(movie);
        rating.setUser(user);
        rating.setRatingValue(rating.getRatingValue());
        user.getRating().add(rating);

        userServiceImpl.save(user);

        return new ResponseEntity<>("Rating created successfully.", HttpStatus.CREATED);
    }
}