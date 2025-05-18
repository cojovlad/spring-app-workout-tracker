package com.example.spring_app_workout_tracker.exception.ExceptionHandlers;

import com.example.spring_app_workout_tracker.controller.AuthController;
import com.example.spring_app_workout_tracker.exception.CustomAppException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Locale;

import static com.example.spring_app_workout_tracker.util.MessageKeys.ERROR;

@ControllerAdvice(assignableTypes = AuthController.class)
public class AuthControllerExceptionHandler {

    private final MessageSource messageSource;

    @Autowired
    public AuthControllerExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(CustomAppException.class)
    public String handleCustomException(CustomAppException exception,
                                        RedirectAttributes redirectAttributes,
                                        Locale locale) {
        String errorMessage;
        try {
            errorMessage = messageSource.getMessage(exception.getMessage(), null, locale);
        } catch (Exception ex) {
            errorMessage = exception.getMessage();
        }
        redirectAttributes.addFlashAttribute(ERROR, errorMessage);
        return "redirect:/api/v1/auth/register";
    }
}
