package com.example.MovieService.controllers.MovieControllers;

import com.example.MovieService.models.Movie;
import com.example.MovieService.sevices.MovieServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/movies/watch")
@Api(tags = "MovieStreamingController API")
public class StreamMovieController {
    private final MovieServiceImpl movieServiceImpl;

    @Autowired
    public StreamMovieController(MovieServiceImpl movieServiceImpl) {
        this.movieServiceImpl = movieServiceImpl;
    }

    @ApiOperation("Watch movie by movie ID")
    @GetMapping("/{id}")
    public ResponseEntity<String> getMovieVideoByMovieId(@PathVariable long id) {
        log.info("Received request to get movie video for movie ID: {}", id);
        Movie movieById = movieServiceImpl.findMovieById(id);
        String videoUrl = movieById.getMovieLink();
        log.info("Returning movie video URL: {}", videoUrl);
        return ResponseEntity.ok(videoUrl);
    }

    @ApiOperation("Watch movie by trailer ID")
    @GetMapping("/trailer/{id}")
    public ResponseEntity<String> getTrailerByMovieId(@PathVariable long id) {
        log.info("Received request to get trailer for movie ID: {}", id);
        Movie movieById = movieServiceImpl.findMovieById(id);
        String trailerUrl = movieById.getTrailerLink();
        log.info("Returning trailer URL: {}", trailerUrl);
        return ResponseEntity.ok(trailerUrl);
    }

}
