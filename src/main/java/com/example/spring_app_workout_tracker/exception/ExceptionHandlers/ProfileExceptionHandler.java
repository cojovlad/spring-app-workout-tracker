package com.example.spring_app_workout_tracker.exception.ExceptionHandlers;

import com.example.spring_app_workout_tracker.controller.ProfileController;
import com.example.spring_app_workout_tracker.exception.CustomAppException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Locale;

import static com.example.spring_app_workout_tracker.util.MessageKeys.ERROR;

@ControllerAdvice(assignableTypes = ProfileController.class)
public class ProfileExceptionHandler {

    private final MessageSource messageSource;

    @Autowired
    public ProfileExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(CustomAppException.class)
    public String handleCustomException(CustomAppException exception,
                                        RedirectAttributes redirectAttributes,
                                        Locale locale) {

        String error = messageSource.getMessage(exception.getMessage(), null, locale);
        redirectAttributes.addFlashAttribute(ERROR, error);

        return "redirect:/api/v1/profile";
    }
}
