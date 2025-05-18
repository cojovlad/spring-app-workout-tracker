package com.example.spring_app_workout_tracker.exception.workout;

public class WorkoutExerciseNotFound extends RuntimeException {
    public WorkoutExerciseNotFound(String workoutExercise) {
        super("Workout exercise not found: " + workoutExercise + " for this exercise id.");
    }
}
