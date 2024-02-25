package com.example.MovieService.controllers.authControllers;

import com.example.MovieService.jwt.JwtTokenProvider;
import com.example.MovieService.models.Role;
import com.example.MovieService.models.User;
import com.example.MovieService.models.dtos.userDtos.UserRegistrationDto;
import com.example.MovieService.sevices.RoleServiceImpl;
import com.example.MovieService.sevices.UserServiceImpl;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Api(tags = "RegistrationController API")
public class RegistrationController {
    private static final Logger logger = LoggerFactory.getLogger(RegistrationController.class);
    private final UserServiceImpl userServiceImpl;
    private final JwtTokenProvider jwtTokenProvider;
    private final RoleServiceImpl roleServiceImpl;

    @Autowired
    public RegistrationController(UserServiceImpl userServiceImpl, JwtTokenProvider jwtTokenProvider, RoleServiceImpl roleServiceImpl) {
        this.userServiceImpl = userServiceImpl;
        this.jwtTokenProvider = jwtTokenProvider;
        this.roleServiceImpl = roleServiceImpl;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRegistrationDto registrationDto) {
        String username = registrationDto.getUsername();
        logger.info("Registration attempt for username: {}", username);
        if (userServiceImpl.userExists(username)) {
            logger.info("Username '{}' is available for registration", username);
            User newUser = new User();
            newUser.setUsername(username);
            newUser.setPassword(new BCryptPasswordEncoder().encode(registrationDto.getPassword()));
            if (registrationDto.getRole() != null && !registrationDto.getRole().isEmpty()) {
                Role role = roleServiceImpl.findRoleByName(registrationDto.getRole());
                newUser.setRoles(Collections.singleton(role));
            } else {
                Role defaultRole = roleServiceImpl.findRoleByName("ROLE_USER");
                newUser.setRoles(Collections.singleton(defaultRole));
            }
            userServiceImpl.save(newUser);

            String token = jwtTokenProvider.createToken(newUser.getUsername(), newUser.getRoles());

            Map<Object, Object> response = new HashMap<>();
            response.put("username", newUser.getUsername());
            response.put("token", token);

            logger.info("User registered successfully: {}", newUser.getUsername());
            return ResponseEntity.ok(response);
        } else {
            logger.warn("Registration attempt failed - Username already exists: {}", username);
            return ResponseEntity.badRequest().body("Username already exists");
        }
    }
}
