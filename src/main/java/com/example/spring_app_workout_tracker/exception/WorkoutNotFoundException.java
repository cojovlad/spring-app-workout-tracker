package com.example.spring_app_workout_tracker.exception;

public class WorkoutNotFoundException extends RuntimeException {
    public WorkoutNotFoundException(Long message) {
        super(String.valueOf(message));
    }
}
