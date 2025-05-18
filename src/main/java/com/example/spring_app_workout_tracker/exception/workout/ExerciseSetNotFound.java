package com.example.spring_app_workout_tracker.exception.workout;

public class ExerciseSetNotFound extends RuntimeException {
    public ExerciseSetNotFound(String exerciseSet) {
        super("Exercise not found "+exerciseSet+ " for this exercise set id.");
    }
}
