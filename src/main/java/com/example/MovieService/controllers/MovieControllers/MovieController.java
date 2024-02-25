package com.example.MovieService.controllers.MovieControllers;

import com.example.MovieService.models.dtos.movieDtos.MovieDetailsDto;
import com.example.MovieService.models.dtos.movieDtos.MovieDto;
import com.example.MovieService.models.dtos.movieDtos.MovieFilterDTO;
import com.example.MovieService.sevices.MovieServiceImpl;
import com.example.MovieService.sevices.UserServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@Slf4j
@RequestMapping("/movies")
@Api(tags = "MovieController API")
@CrossOrigin
public class MovieController {
    private final MovieServiceImpl service;
    private final UserServiceImpl userService;

    @Autowired
    public MovieController(MovieServiceImpl service, UserServiceImpl userService) {
        this.service = service;
        this.userService = userService;
    }

    @ApiOperation("Get movie details by movie ID")
    @GetMapping("/{id}")
    public ResponseEntity<MovieDetailsDto> getMovieDetails(@PathVariable("id") Long id) {
        userService.addToViewedMovies(
                id, SecurityContextHolder.getContext().getAuthentication());
        return ResponseEntity.ok(service.findById(id));
    }

    @ApiOperation("Get all movies")
    @GetMapping
    public ResponseEntity<List<MovieDto>> findAll(
            @RequestParam(required = false, defaultValue = "by_date") String sortType) {
        return ResponseEntity.ok(service.getMovies(sortType));
    }

    @ApiOperation("search movies")
    @GetMapping("/search")
    public ResponseEntity<List<MovieDto>> search(@RequestBody MovieFilterDTO dto) {
        return ResponseEntity.ok(service.getFilteredMovies(dto));
    }
}
