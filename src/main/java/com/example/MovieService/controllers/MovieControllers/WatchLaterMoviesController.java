package com.example.MovieService.controllers.MovieControllers;

import com.example.MovieService.models.User;
import com.example.MovieService.sevices.UserServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Api(tags = "WatchLaterMoviesController API")
@RestController
@RequestMapping("/watchLaterMovies")
public class WatchLaterMoviesController {
    private final UserServiceImpl service;

    @Autowired
    public WatchLaterMoviesController(UserServiceImpl service) {
        this.service = service;
    }

    @ApiOperation("add movie to watch later list")
    @PostMapping("/{movieId}")
    public ResponseEntity<String> addWatchLaterMovie(@PathVariable("movieId") long movieId) {
        User user = service.findByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName());

        service.addMovieToList(user, movieId, user.getWatchLaterMovies());
        return ResponseEntity.ok("Movie added to watch later");
    }

    @ApiOperation("remove movie from watch later list")
    @DeleteMapping("/{movieId}")
    public ResponseEntity<String> removeWatchLaterMovie(@PathVariable("movieId") long movieId) {
        User user = service.findByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName());

        service.removeFromList(user, movieId, user.getWatchLaterMovies());
        return ResponseEntity.ok("Movie removed from watch later");
    }
}