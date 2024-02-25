package com.example.MovieService.models.dtos.userDtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EditUserDto {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    private String newUsername;
    private String newPassword;
}
