package com.example.MovieService.sevices;

import com.example.MovieService.models.Movie;
import com.example.MovieService.models.Role;
import com.example.MovieService.models.User;
import com.example.MovieService.models.dtos.userDtos.UserDto;
import com.example.MovieService.models.dtos.userDtos.EditUserDto;
import com.example.MovieService.repositories.UserRepository;
import com.example.MovieService.sevices.interfaces.UserService;
import com.example.MovieService.utils.mappers.user.UserMapper;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserDetailsService, UserService {
    private final UserRepository repository;
    private final MovieServiceImpl movieService;
    private final BCryptPasswordEncoder encoder;

    @Autowired
    public UserServiceImpl(UserRepository repository, MovieServiceImpl movieService, @Lazy BCryptPasswordEncoder encoder) {
        this.repository = repository;
        this.movieService = movieService;
        this.encoder = encoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findByUsername(username);
        if (user == null) {
            log.info("User is not found.");
            throw new UsernameNotFoundException("User is not found");
        }
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), user.getPassword(), getAuthorities(user.getRoles()));

    }

    private Set<SimpleGrantedAuthority> getAuthorities(Set<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
                .collect(Collectors.toSet());
    }

    @Override
    public User findByUsername(String username) {
        log.info("Retrieved optional user with username {} ", username);
        return Objects.requireNonNull(repository.findByUsername(username));
    }

    @Override
    public boolean userExists(String username) {
        log.info("Retrieved user with username {} ", username);
        User user = repository.findByUsername(username);
        if (user == null) {
            log.info("User is not found.");
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void removeFromList(User user, long movieId, List<Movie> movies) {
        Movie movie = movieService.findMovieById(movieId);
        if (movies.contains(movie)) {
            movies.remove(movie);
            log.info("Removed movie with id {}", movieId);
            repository.save(user);
        } else {
            log.info("Movie with ID {} already in list for user {}.", movie.getId(), user.getUsername());
            throw new RuntimeException("Movie already in list");
        }
    }

    @Override
    public User save(User user) {
        log.info("Save user with username {} ", user.getUsername());
        return repository.save(Objects.requireNonNull(user, "User cannot be null."));
    }

    @Override
    public User addMovieToList(User user, long movieId, List<Movie> movies) {
        Movie movie = movieService.findMovieById(movieId);
        if (!movies.stream().anyMatch(m -> m.getId() == movieId)) {
            log.info("Movie with ID {} added to list for user {}.", movie.getId(), user.getUsername());
            movies.add(movie);
            return repository.save(user);
        } else {
            log.info("Movie with ID {} already in list for user {}.", movie.getId(), user.getUsername());
            throw new RuntimeException("Movie already in list");
        }
    }

    @Override
    public User updateUser(EditUserDto editUser, User user) {
        log.info("Updating user with ID {} ", user.getId());
        if (isPasswordAndUsernameValid(editUser, user)) {
            if (editUser.getNewUsername() != null) {
                log.info("Updating username");
                user.setUsername(editUser.getNewUsername());
            }
            if (editUser.getNewPassword() != null) {
                log.info("Updating password");
                user.setPassword(encoder.encode(editUser.getNewPassword()));
            }
            log.info("User updated successfully");
            return repository.save(user);
        } else {
            throw new RuntimeException("Wrong password or username");
        }
    }

    private boolean isPasswordAndUsernameValid(EditUserDto editUser, User user) {
        log.info("Checking if password and username are valid");
        return encoder.matches(editUser.getPassword(), user.getPassword()) && editUser.getUsername().equals(user.getUsername());
    }

    @Override
    public List<UserDto> findAllUsers() {
        log.info("Fetching all users");
        return repository.findAll()
                .stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto findUserById(Long id) {
        log.info("Fetching user with ID: " + id);
        try {
            User user = repository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Not found"));
            return UserMapper.mapToUserDto(user);
        } catch (NotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addToViewedMovies(long id, Authentication auth){
        if (!userExists(auth.getName())) {
            User user = findByUsername(auth.getName());
            if (!isMovieInList(user.getViewedMovies(), id)) {
                addMovieToList(user, id, user.getViewedMovies());
            }
        }
    }
    private boolean isMovieInList(List<Movie> viewedMovies, long id) {
        return viewedMovies.stream().anyMatch(m -> m.getId().equals(id));
    }
}
