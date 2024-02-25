package com.example.MovieService.controllers.MovieControllers;

import com.example.MovieService.models.User;
import com.example.MovieService.sevices.UserServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Api(tags = "FavouriteMoviesController API")
@RestController
@RequestMapping("/favoriteMovies")
public class FavouriteMoviesController {
    private final UserServiceImpl service;

    @Autowired
    public FavouriteMoviesController(UserServiceImpl service) {
        this.service = service;
    }

    @ApiOperation("add movie to favourite list")
    @PostMapping("/{movieId}")
    public ResponseEntity<String> addFavoriteMovie(@PathVariable("movieId") long movieId) {
        User user = service.findByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName());

        service.addMovieToList(user, movieId, user.getFavoriteMovies());
        return ResponseEntity.ok("Movie added to favorites");
    }

    @ApiOperation("remove movie from favourite list")
    @DeleteMapping("/{movieId}")
    public ResponseEntity<String> removeFavoriteMovie(@PathVariable("movieId") long movieId) {
        User user = service.findByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName());

        service.removeFromList(user, movieId, user.getFavoriteMovies());
        return ResponseEntity.ok("Movie removed from favorites");
    }
}