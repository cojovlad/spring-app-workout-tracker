package com.example.spring_app_workout_tracker.controller;

import com.example.spring_app_workout_tracker.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

import static com.example.spring_app_workout_tracker.util.MessageKeys.SUCCESS;

@Controller
@RequestMapping("api/v1/language")
public class LanguageController {

    private final UserService userService;
    private final SessionLocaleResolver localeResolver;
    private final MessageSource messageSource;

    @Autowired
    public LanguageController(UserService userService, SessionLocaleResolver localeResolver, MessageSource messageSource) {
        this.userService = userService;
        this.localeResolver = localeResolver;
        this.messageSource = messageSource;
    }

    @PostMapping("/set")
    public String setLanguage(
            @RequestParam String code,
            Authentication authentication,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes,
            Locale locale) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/api/v1/auth/login";
        }

        String username = authentication.getName();
        userService.updateUserLanguage(username, code);

        Locale selectedLocale = Locale.forLanguageTag(code);
        localeResolver.setLocale(request, null, selectedLocale);

        String successMessage = messageSource.getMessage("success.language.changed", null, locale);
        redirectAttributes.addFlashAttribute(SUCCESS, successMessage);

        return "redirect:/api/v1/dashboard";
    }
}
