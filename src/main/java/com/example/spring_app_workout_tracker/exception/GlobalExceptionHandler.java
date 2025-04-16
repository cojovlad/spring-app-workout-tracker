package com.example.spring_app_workout_tracker.exception;

import com.example.spring_app_workout_tracker.dto.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    @Autowired
    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(CustomAppException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomAppException ex, Locale locale) {
        String localizedMessage = messageSource.getMessage(ex.getMessageKey(), ex.getArgs(), locale);
        ErrorResponse error = new ErrorResponse(localizedMessage, ex.getCode());
        return new ResponseEntity<>(error, ex.getStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(Exception ex, Locale locale) {
        String localizedMessage;
        if (ex.getMessage() != null) {
            localizedMessage = messageSource.getMessage("error.unexpected", new Object[]{ex.getMessage()}, locale);
        } else {
            localizedMessage = messageSource.getMessage("error.unexpected", null, locale);
        }

        ErrorResponse error = new ErrorResponse(localizedMessage, "GENERIC_ERROR");
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
