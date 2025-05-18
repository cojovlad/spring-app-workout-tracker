package com.example.spring_app_workout_tracker.exception;

public class LanguageNotFoundException extends RuntimeException {
    public LanguageNotFoundException(String language) {
        super("Language could not be found:"+" "+language);
    }
}
