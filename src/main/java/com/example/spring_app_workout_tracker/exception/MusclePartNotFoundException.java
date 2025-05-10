package com.example.spring_app_workout_tracker.exception;

public class MusclePartNotFoundException extends RuntimeException {
    public MusclePartNotFoundException(String muscleName) {
        super("Muscle part not found: " + muscleName);
    }
}