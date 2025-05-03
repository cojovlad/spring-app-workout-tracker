package com.example.spring_app_workout_tracker.service;

import com.example.spring_app_workout_tracker.entity.workout.Exercise;

public interface ExerciseService {
    Exercise updateExerciseName(Long id, String name);

    Exercise updateExercise(Long id, String name, String description);

    void deleteExercise(Long id);

    void addMuscleTarget(Long exerciseId, Long musclePartId);

    void removeMuscleTarget(Long exerciseId, Long musclePartId);

    void updateExerciseMuscle(Long exerciseId, Long newMusclePartId);
}
