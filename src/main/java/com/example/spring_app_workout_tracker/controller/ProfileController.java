package com.example.spring_app_workout_tracker.controller;

import com.example.spring_app_workout_tracker.dto.PasswordChangeForm;
import com.example.spring_app_workout_tracker.entity.User;
import com.example.spring_app_workout_tracker.exception.CustomAppException;
import com.example.spring_app_workout_tracker.service.UserService;
import jakarta.validation.Valid;
import org.springframework.context.MessageSource;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Locale;

@Controller
@RequestMapping("api/v1/profile")
public class ProfileController {

    private final UserService userService;
    private final MessageSource messageSource;

    public ProfileController(UserService userService, MessageSource messageSource) {
        this.userService = userService;
        this.messageSource = messageSource;
    }

    @GetMapping("")
    public String showProfile(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", user);
        if (!model.containsAttribute("passwordChangeForm")) {
            model.addAttribute("passwordChangeForm", new PasswordChangeForm());
        }
        return "profile";
    }

    @PostMapping("/change-password")
    public String changePassword(
            @AuthenticationPrincipal User user,
            @Valid @ModelAttribute("passwordChangeForm") PasswordChangeForm form,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Locale locale) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.passwordChangeForm", bindingResult);
            redirectAttributes.addFlashAttribute("passwordChangeForm", form);
            return "redirect:/api/v1/profile";
        }

        if (!form.getNewPassword().equals(form.getConfirmPassword())) {
            String mismatchMessage = messageSource.getMessage("error.password.mismatch", null, locale);
            redirectAttributes.addFlashAttribute("error", mismatchMessage);
            redirectAttributes.addFlashAttribute("passwordChangeForm", form);
            return "redirect:/api/v1/profile";
        }

        try {
            userService.changePassword(user.getUsername(), form.getCurrentPassword(), form.getNewPassword());
            String successMessage = messageSource.getMessage("success.password.changed", null, locale);
            redirectAttributes.addFlashAttribute("success", successMessage);
        } catch (CustomAppException e) {
            String errorMessage = messageSource.getMessage(e.getMessage(), null, locale);
            redirectAttributes.addFlashAttribute("error", errorMessage);
            redirectAttributes.addFlashAttribute("passwordChangeForm", form);
        }

        return "redirect:/api/v1/profile";
    }
}