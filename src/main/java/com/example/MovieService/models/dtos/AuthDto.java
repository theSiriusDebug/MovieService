package com.example.MovieService.models.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuthDto {
    private String username;
    private String login;
    private String password;
}