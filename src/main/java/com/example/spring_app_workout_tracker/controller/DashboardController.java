package com.example.spring_app_workout_tracker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("api/v1/dashboard")
public class DashboardController {

    @GetMapping("")
    public String showMainPage() {
        return "dashboard";
    }
}
