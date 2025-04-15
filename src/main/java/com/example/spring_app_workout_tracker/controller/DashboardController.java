package com.example.spring_app_workout_tracker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("api/v1")
public class DashboardController {

    @GetMapping("/dashboard")
    public String showMainPage() {
        return "dashboard";
    }
}
