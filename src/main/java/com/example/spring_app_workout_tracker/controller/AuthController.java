package com.example.spring_app_workout_tracker.controller;

import com.example.spring_app_workout_tracker.entity.User;
import com.example.spring_app_workout_tracker.exception.CustomAppException;
import com.example.spring_app_workout_tracker.service.LanguageService;
import com.example.spring_app_workout_tracker.service.UserService;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Locale;

import static com.example.spring_app_workout_tracker.util.MessageKeys.*;

@Controller
@RequestMapping("api/v1/auth")
public class AuthController {

    private final UserService userService;
    private final LanguageService languageService;
    private final MessageSource messageSource;

    public AuthController(UserService userService, LanguageService languageService, MessageSource messageSource) {
        this.userService = userService;
        this.languageService = languageService;
        this.messageSource = messageSource;
    }

    @GetMapping("/login")
    public String showLoginForm(
            @RequestParam(required = false) String rememberMe,
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout,
            @RequestParam(value = "registered", required = false) String registered,
            Authentication authentication,
            Model model,
            Locale locale) {

        if (authentication != null && authentication.isAuthenticated()) {
            return "redirect:/api/v1/dashboard";
        }

        if (error != null) {
            String errorMessage = messageSource.getMessage("error.invalid.credentials", null, locale);
            model.addAttribute(ERROR, errorMessage);
        }
        if (logout != null) {
            String logoutMessage = messageSource.getMessage("success.logout", null, locale);
            model.addAttribute(LOGOUT_MESSAGE, logoutMessage);
        }
        if (registered != null) {
            String registeredMessage = messageSource.getMessage("success.registration", null, locale);
            model.addAttribute(MESSAGE, registeredMessage);
        }
        return "login";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute(USER, new User());
        model.addAttribute(LANGUAGES, languageService.getAllLanguages());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user,
                               RedirectAttributes redirectAttributes,
                               Locale locale) {
        userService.createUser(user);
        String successMessage = messageSource.getMessage("success.registration", null, locale);
        redirectAttributes.addFlashAttribute(SUCCESS, successMessage);
        return "redirect:/api/v1/auth/login";
    }
}