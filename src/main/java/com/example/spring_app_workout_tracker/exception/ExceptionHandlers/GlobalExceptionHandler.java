package com.example.spring_app_workout_tracker.exception.ExceptionHandlers;

import com.example.spring_app_workout_tracker.dto.ErrorResponse;
import com.example.spring_app_workout_tracker.exception.CustomAppException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Locale;

import static com.example.spring_app_workout_tracker.util.MessageKeys.ERROR;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    @Autowired
    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(CustomAppException.class)
    public String handleCustomException(CustomAppException ex,
                                        RedirectAttributes redirectAttributes,
                                        Locale locale) {

        String message = messageSource.getMessage(ex.getMessageKey(), ex.getArgs(), locale);

        redirectAttributes.addFlashAttribute(ERROR, message);

        return "redirect:/api/v1/dashboard";
    }

    @ExceptionHandler(Exception.class)
    public String handleGeneral(Exception ex,
                                RedirectAttributes redirectAttributes,
                                Locale locale) {

        String localizedMessage;

        if (ex.getMessage() != null) {
            localizedMessage = messageSource.getMessage("error.unexpected", new Object[]{ex.getMessage()}, locale);
        } else {
            localizedMessage = messageSource.getMessage("error.unexpected", null, locale);
        }

        redirectAttributes.addFlashAttribute(ERROR, localizedMessage);

        return "redirect:/api/v1/dashboard";
    }
}
