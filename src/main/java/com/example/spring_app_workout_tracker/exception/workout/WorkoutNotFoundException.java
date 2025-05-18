package com.example.spring_app_workout_tracker.exception.workout;

public class WorkoutNotFoundException extends RuntimeException {
    public WorkoutNotFoundException(Long message) {
        super("Workout not found"+ String.valueOf(message) + " for this workout id.");
    }
}
