package com.example.MovieService.controllers.MovieControllers.Parser;

import com.example.MovieService.parser.MovieDetailsExtractor;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@Api(tags = "ParserController API")
public class MovieParsingController {
    private final MovieDetailsExtractor movieDetailsExtractor;
    private final ExecutorService executorService;

    @Autowired
    public MovieParsingController(MovieDetailsExtractor movieDetailsExtractor) {
        this.movieDetailsExtractor = movieDetailsExtractor;
        this.executorService = Executors.newFixedThreadPool(5);
    }

    @ApiOperation("Parse movie details")
    @PostMapping("/parse")
    public void parseMovies() {
        long latestPage = 10;
        long currentPage = 1;

        while (currentPage <= latestPage) {
            final long page = currentPage;
            executorService.submit(() -> parseMoviePage(page));
            currentPage++;
        }

        executorService.shutdown();

        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            log.info("All parsing tasks completed.");
        } catch (InterruptedException e) {
            log.error("Error waiting for parsing tasks to complete: {}", e.getMessage());
        }
    }

    private void parseMoviePage(long page) {
        try {
            String url = String.format("https://m.anwap.love/films/%d", page);
            String html = movieDetailsExtractor.fetchHtml(url);
            movieDetailsExtractor.parseMovieDetails(html);
            log.info("Successfully parsed page {}", page);
        } catch (IOException e) {
            log.info("Error parsing page {}", page);
            throw new RuntimeException(e);
        }
    }
}