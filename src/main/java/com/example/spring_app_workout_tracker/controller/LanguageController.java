package com.example.spring_app_workout_tracker.controller;

import com.example.spring_app_workout_tracker.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

@Controller
@RequestMapping("api/v1/language")
public class LanguageController {

    private final UserService userService;
    private final SessionLocaleResolver localeResolver;

    @Autowired
    public LanguageController(UserService userService, SessionLocaleResolver localeResolver) {
        this.userService = userService;
        this.localeResolver = localeResolver;
    }

    @PostMapping("/set")
    public String setLanguage(@RequestParam String code, Authentication authentication, HttpServletRequest request) {
        String username = authentication.getName();

        userService.updateUserLanguage(username, code);

        Locale selectedLocale = new Locale(code);
        localeResolver.setLocale(request, null, selectedLocale);  // Apply locale to the session

        return "redirect:/api/v1/dashboard";
    }
}
