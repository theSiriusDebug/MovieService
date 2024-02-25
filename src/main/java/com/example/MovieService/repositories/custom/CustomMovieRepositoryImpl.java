package com.example.MovieService.repositories.custom;

import com.example.MovieService.models.Movie;
import com.example.MovieService.models.dtos.movieDtos.MovieDto;
import com.example.MovieService.models.dtos.movieDtos.MovieFilterDTO;
import com.example.MovieService.utils.mappers.movie.MovieMapper;
import com.example.MovieService.utils.sorting.SortingUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class CustomMovieRepositoryImpl implements CustomMovieRepository{
    private final EntityManager entityManager;

    @Autowired
    public CustomMovieRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<MovieDto> findMovies(MovieFilterDTO filterDto) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Movie> query = builder.createQuery(Movie.class);

        Root<Movie> root = query.from(Movie.class);
        List<Predicate> predicates = new ArrayList<>();
        if (filterDto.getTitle()!= null) {
            predicates.add(builder.like(root.get("title"), "%" + filterDto.getTitle() + "%"));
        }
        if (filterDto.getLanguage()!= null) {
            predicates.add(builder.equal(root.get("language"),filterDto.getLanguage()));
        }
        if (filterDto.getDirector()!= null) {
            predicates.add(builder.equal(root.get("director"), filterDto.getDirector()));
        }
        if (filterDto.getCountry()!= null) {
            predicates.add(builder.equal(root.get("country"), filterDto.getCountry()));
        }
        if (filterDto.getGenre()!= null) {
            predicates.add(builder.equal(root.get("genres"), filterDto.getGenre()));
        }
        if (filterDto.getDurationMax()!= null) {
            predicates.add(builder.lessThanOrEqualTo(root.get("duration"), filterDto.getCountry()));
        }
        if (filterDto.getDurationMin()!= null) {
            predicates.add(builder.greaterThanOrEqualTo(root.get("duration"), filterDto.getCountry()));
        }
        if (filterDto.getMaxYear()!= null) {
            predicates.add(builder.lessThanOrEqualTo(root.get("year"), filterDto.getMaxYear()));
        }
        if (filterDto.getMinYear()!= null) {
            predicates.add(builder.greaterThanOrEqualTo(root.get("year"), filterDto.getMinYear()));
        }
        if (filterDto.getMaxImdbRating()!= null) {
            predicates.add(builder.lessThanOrEqualTo(root.get("imdbRating"), filterDto.getMaxImdbRating()));
        }
        if (filterDto.getMinImdbRating()!= null) {
            predicates.add(builder.greaterThanOrEqualTo(root.get("imdbRating"), filterDto.getMinImdbRating()));
        }
        if (filterDto.getMaxKinopoiskRating()!= null) {
            predicates.add(builder.lessThanOrEqualTo(root.get("kinopoiskRating"), filterDto.getMaxKinopoiskRating()));
        }
        if (filterDto.getMinKinopoiskRating()!= null) {
            predicates.add(builder.greaterThanOrEqualTo(root.get("kinopoiskRating"), filterDto.getMinKinopoiskRating()));
        }

        query.where(predicates.toArray(new Predicate[0]));

        if (filterDto.getSortCriteria() != null) {
            query.orderBy(QueryUtils.toOrders(SortingUtil.getSorting(filterDto.getSortCriteria()), root, builder));
        } else {
            query.orderBy(QueryUtils.toOrders(Sort.by(Sort.Direction.DESC,"year"), root, builder));
        }
        return entityManager.createQuery(query).getResultList().stream()
                .map(MovieMapper::mapToMovieDto)
                .collect(Collectors.toList());
    }
}
