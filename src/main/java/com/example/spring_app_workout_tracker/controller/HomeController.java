package com.example.spring_app_workout_tracker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller responsible for handling requests to the application's root context path
 * and redirecting them to the authentication login page.
 * <p>
 * This controller ensures that users accessing the base URL or root path of the application
 * are automatically redirected to the login page for authentication.
 * </p>
 */
@Controller
public class HomeController {

    /**
     * Handles GET requests to the root ("/") and default ("") application paths.
     * <p>
     * Performs a 302 redirect to the authentication login endpoint located at {@code /api/v1/auth/login}.
     * This ensures users are automatically directed to the login page when accessing the application's
     * base URL.
     * </p>
     *
     * @return A redirect instruction string that forwards to the versioned login endpoint
     * @see org.springframework.web.bind.annotation.GetMapping
     */
    @GetMapping({"", "/"})
    public String redirectToLogin() {
        return "redirect:/api/v1/auth/login";
    }
}