package com.example.MovieService.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(tags = "Home API")
public class HomeController {
    @ApiOperation("Get home page")
    @GetMapping("/")
    public String getHomePage() {
        return "homePage";
    }
}
