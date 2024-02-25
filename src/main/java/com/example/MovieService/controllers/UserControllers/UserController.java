package com.example.MovieService.controllers.UserControllers;

import com.example.MovieService.models.User;
import com.example.MovieService.models.dtos.userDtos.UserDto;
import com.example.MovieService.models.dtos.userDtos.EditUserDto;
import com.example.MovieService.sevices.UserServiceImpl;
import com.example.MovieService.utils.mappers.user.UserMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(tags = "UserController")
public class UserController {
    private final UserServiceImpl service;

    @Autowired
    public UserController(UserServiceImpl service) {
        this.service = service;
    }

    @ApiOperation("Update user")
    @PostMapping("/edit")
    public ResponseEntity<User> updateUser(@RequestBody EditUserDto editUser) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok(service.updateUser(editUser, service.findByUsername(auth.getName())));
    }

    @ApiOperation("Retrieve all users")
    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> get(){
        return ResponseEntity.ok(service.findAllUsers());
    }

    @ApiOperation("Get user by id")
    @GetMapping("/users/{id}")
    public ResponseEntity<UserDto> get(@PathVariable("id") long id){
        return ResponseEntity.ok(service.findUserById(id));
    }

    @ApiOperation("Get user profile")
    @GetMapping("/profile")
    public ResponseEntity<UserDto> getUserProfile() {
        return ResponseEntity.ok(UserMapper.mapToUserDto(
                service.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName())));
    }
}
