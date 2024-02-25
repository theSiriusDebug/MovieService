package com.example.MovieService.repositories;

import com.example.MovieService.models.Movie;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    Movie findById(long id);
    Optional<Movie> findById(Long id);
    @Query("SELECT m FROM Movie m WHERE m.title LIKE %?1%")
    List<Movie> search(String title, Sort sorting);
}
