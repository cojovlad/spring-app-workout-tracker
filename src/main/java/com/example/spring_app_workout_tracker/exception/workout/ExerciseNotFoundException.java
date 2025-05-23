package com.example.spring_app_workout_tracker.exception.workout;

public class ExerciseNotFoundException extends RuntimeException {
    public ExerciseNotFoundException(String exerciseName) {
        super("Exercise not found: " + exerciseName);
    }
}
