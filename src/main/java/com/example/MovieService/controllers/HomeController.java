package com.example.MovieService.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String homePage() {
        return "homePage"; // Возвращает имя шаблона домашней страницы (например, home.html)
    }
}
