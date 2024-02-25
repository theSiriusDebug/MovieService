package com.example.MovieService.models.dtos.userDtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserRegistrationDto {
    private String username;
    private String password;
    private String role;
}
