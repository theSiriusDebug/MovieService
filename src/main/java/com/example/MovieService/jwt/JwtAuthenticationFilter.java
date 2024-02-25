package com.example.MovieService.jwt;

import com.example.MovieService.models.User;
import com.example.MovieService.models.dtos.userDtos.AuthDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            AuthDto authRequest = new ObjectMapper().readValue(request.getInputStream(), AuthDto.class);
            logger.info("Attempting authentication for user: {}", authRequest.getUsername());

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    authRequest.getUsername(),
                    authRequest.getPassword(),
                    new ArrayList<>()
            );
            return authenticationManager.authenticate(authentication);
        } catch (IOException e) {
            logger.error("Error reading authentication request", e);
            throw new AuthenticationServiceException("Error reading authentication request", e);        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
        User principal = (User) authResult.getPrincipal();
        String token = jwtTokenProvider.createToken(principal.getUsername(), principal.getRoles());

        logger.info("Successful authentication for user: {}", principal.getUsername());

        response.addHeader("Authorization", "Bearer " + token);
    }
}