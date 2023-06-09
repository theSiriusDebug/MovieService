package com.example.MovieService.controllers;

import com.example.MovieService.models.User;
import com.example.MovieService.models.dtos.UserRegistrationDto;
import com.example.MovieService.repositories.UserRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.Optional;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(tags = "Home API")
public class HomeController {
    @Autowired
    private UserRepository userRepository;

    private static final Logger logger = Logger.getLogger(HomeController.class);

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @ApiOperation("Get home page")
    @GetMapping("/")
    public String homePage() {
        return "homePage";
    }

    @ApiOperation("Edit user")
    @GetMapping("/edit")
    public ResponseEntity<?> editUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();

        Optional<User> existingUserOptional = userRepository.findByUsername(currentUserName);
        if (existingUserOptional.isPresent()) {
            User existingUser = existingUserOptional.get();
            return ResponseEntity.ok(existingUser);
        }
        User existingUser = existingUserOptional.get();

        return ResponseEntity.ok(existingUser);
    }

    @ApiOperation("Update user")
    @PostMapping("/edit")
    public ResponseEntity<?> updateUser(@RequestBody UserRegistrationDto userRegistrationDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();

        Optional<User> existingUserOptional = userRepository.findByUsername(currentUserName);
        if (!existingUserOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        User existingUser = existingUserOptional.get();

        existingUser.setLogin(userRegistrationDto.getLogin());
        existingUser.setPassword(passwordEncoder.encode(userRegistrationDto.getPassword()));

        userRepository.save(existingUser);

        return ResponseEntity.ok(existingUser);
    }


    @ApiOperation("Get user profile")
    @GetMapping("/profile")
    public UserRegistrationDto profile(){
        return new UserRegistrationDto();
    }
}
