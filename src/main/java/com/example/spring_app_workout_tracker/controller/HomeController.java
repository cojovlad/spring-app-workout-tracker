package com.example.spring_app_workout_tracker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("")
    public String redirectToLoginNoSlash() {
        return "redirect:/api/v1/auth/login";
    }

    @GetMapping("/")
    public String redirectToLoginSlash() {
        return "redirect:/api/v1/auth/login";
    }
}
