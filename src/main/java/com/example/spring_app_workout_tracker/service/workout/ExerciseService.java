package com.example.spring_app_workout_tracker.service.workout;

import com.example.spring_app_workout_tracker.entity.workout.Exercise;

public interface ExerciseService {
    Exercise updateExerciseName(Long id, String name);

    void deleteExercise(Long id);

    void addMuscleTarget(Long exerciseId, Long musclePartId);

    void removeMuscleTarget(Long exerciseId, Long musclePartId);

    void updateExerciseMuscle(Long exerciseId, Long newMusclePartId);
}