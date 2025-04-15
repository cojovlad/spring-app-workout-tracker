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

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(CustomAppException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomAppException ex, Locale locale) {
        String localizedMessage = messageSource.getMessage(ex.getMessageKey(), ex.getArgs(), locale);
        ErrorResponse error = new ErrorResponse(localizedMessage, ex.getCode());
        return new ResponseEntity<>(error, ex.getStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(Exception ex) {
        ErrorResponse error = new ErrorResponse("Unexpected error occurred", "GENERIC_ERROR");
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
