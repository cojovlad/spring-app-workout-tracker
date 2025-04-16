package com.example.spring_app_workout_tracker.controller;

import com.example.spring_app_workout_tracker.entity.User;
import com.example.spring_app_workout_tracker.exception.CustomAppException;
import com.example.spring_app_workout_tracker.service.LanguageService;
import com.example.spring_app_workout_tracker.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("api/v1/auth")
public class AuthController {

    private final UserService userService;
    private final LanguageService languageService;

    public AuthController(UserService userService, LanguageService languageService) {
        this.userService = userService;
        this.languageService = languageService;
    }

    @GetMapping("/login")
    public String showLoginForm(
            @RequestParam(required = false) String rememberMe,
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout,
            @RequestParam(value = "registered", required = false) String registered,
            Authentication authentication,
            Model model) {

        if (authentication != null && authentication.isAuthenticated()) {
            return "redirect:/api/v1/dashboard";
        }

        if (error != null) {
            model.addAttribute("error", "error.invalid.credentials");
        }
        if (logout != null) {
            model.addAttribute("message", "success.logout");
        }
        if (registered != null) {
            model.addAttribute("message", "success.registration");
        }
        return "login";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("languages", languageService.getAllLanguages());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
        try {
            userService.createUser(user);
            redirectAttributes.addFlashAttribute("success", "success.registration");
            return "redirect:/api/v1/auth/login";
        } catch (CustomAppException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/api/v1/auth/register";
        }
    }
}