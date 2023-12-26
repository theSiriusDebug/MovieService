package com.example.MovieService.controllers.authControllers;

import com.example.MovieService.jwt.JwtTokenProvider;
import com.example.MovieService.models.Role;
import com.example.MovieService.models.User;
import com.example.MovieService.models.dtos.UserRegistrationDto;
import com.example.MovieService.sevices.RoleService;
import com.example.MovieService.sevices.UserService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
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
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final RoleService roleService;

    @Autowired
    public RegistrationController(
            AuthenticationManager authenticationManager, UserService userService,
            JwtTokenProvider jwtTokenProvider, RoleService roleService) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.roleService = roleService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRegistrationDto registrationDto) {
        try {
            String username = registrationDto.getUsername();
            logger.info("Registration attempt for username: {}", username);
            if (userService.findByUsername(username) == null) {
                logger.info("Username '{}' is available for registration", username);
                User newUser = new User();
                newUser.setUsername(username);
                newUser.setPassword(new BCryptPasswordEncoder().encode(registrationDto.getPassword()));
                if (registrationDto.getRole() != null && !registrationDto.getRole().isEmpty()) {
                    Role role = roleService.findRoleByName(registrationDto.getRole());
                    newUser.setRoles(Collections.singleton(role));
                } else {
                    // Set a default role if no role is provided
                    Role defaultRole = roleService.findRoleByName("ROLE_USER");
                    newUser.setRoles(Collections.singleton(defaultRole));
                }
                userService.save(newUser);

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
        } catch (Exception e) {
            logger.error("An error occurred during user registration: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
