package com.example.spring_app_workout_tracker.exception.workout;

public class ExerciseNotTargetingMuscleException extends RuntimeException {
    public ExerciseNotTargetingMuscleException(String exerciseName, String muscleName) {
        super("Exercise '" + exerciseName + "' doesn't target muscle '" + muscleName + "'");
    }
}
