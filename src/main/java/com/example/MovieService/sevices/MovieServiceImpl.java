package com.example.MovieService.sevices;

import com.example.MovieService.models.Movie;
import com.example.MovieService.models.dtos.movieDtos.MovieDetailsDto;
import com.example.MovieService.models.dtos.movieDtos.MovieDto;
import com.example.MovieService.models.dtos.movieDtos.MovieFilterDTO;
import com.example.MovieService.repositories.MovieRepository;
import com.example.MovieService.repositories.custom.CustomMovieRepositoryImpl;
import com.example.MovieService.sevices.interfaces.MovieService;
import com.example.MovieService.utils.mappers.movie.MovieDetailsMapper;
import com.example.MovieService.utils.mappers.movie.MovieMapper;
import com.example.MovieService.utils.sorting.SortingUtil;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MovieServiceImpl implements MovieService {
    private final MovieRepository repository;
    private final CustomMovieRepositoryImpl customRepository;

    @Autowired
    public MovieServiceImpl(MovieRepository repository, CustomMovieRepositoryImpl customRepository) {
        this.repository = repository;
        this.customRepository = customRepository;
    }

    @Override
    @Cacheable(value = "filteredMovies")
    public List<MovieDto> getFilteredMovies(MovieFilterDTO options) {
        return customRepository.findMovies(options);
    }

    @Override
    @Cacheable("movies")
    public List<MovieDto> getMovies(String sortType) {
        log.info("Retrieving all movies from the database");
        return repository.findAll(SortingUtil.getSorting(sortType)).stream()
                .map(MovieMapper::mapToMovieDto)
                .collect(Collectors.toList());
    }

    @Override
    public Movie findMovieById(long id) {
        log.info("Searching for movie with ID: {}", id);
        return repository.findById(id);
    }

    @Cacheable(value = "movieDetails", key = "#id")
    public MovieDetailsDto findById(long id) {
        log.info("Searching for movie with ID: {}", id);
        return MovieDetailsMapper.mapToMovieDetailsDto(repository.findById(id));
    }

    @Override
    public void saveMovie(@Valid Movie movie) {
        log.info("Saving movie: {}", movie.getTitle());
        repository.save(movie);
    }
}
