package com.example.spring_app_workout_tracker.service.workout;

import com.example.spring_app_workout_tracker.dto.workout.SetRequest;
import com.example.spring_app_workout_tracker.entity.workout.ExerciseSet;

import java.math.BigDecimal;

/**
 * Service interface for managing {@link ExerciseSet} entities within an exercise.
 * Provides operations for creating, updating, and deleting individual sets.
 */
public interface ExerciseSetService {

    /**
     * Creates a new set and associates it with a specific exercise.
     *
     * @param workoutExerciseId the ID of the exercise to which the set will be added
     * @param request           the data used to create the set, including reps, weight, and rest
     * @return the created {@link ExerciseSet} entity
     * @throws IllegalArgumentException if the provided exercise ID or request data is invalid
     */
    ExerciseSet createSet(Long workoutExerciseId, SetRequest request);

    /**
     * Updates an existing exercise set with new values.
     *
     * @param setId       the ID of the set to update
     * @param repetitions the new number of repetitions
     * @param weightKg    the new weight in kilograms
     * @param restSeconds the new rest duration in seconds
     * @return the updated {@link ExerciseSet} entity
     * @throws IllegalArgumentException if any input value is invalid or the set does not exist
     */
    ExerciseSet updateSet(Long setId, Integer repetitions, BigDecimal weightKg, Integer restSeconds);

    /**
     * Deletes a specific set from an exercise.
     *
     * @param setId the ID of the set to delete
     * @throws IllegalArgumentException if the set does not exist or cannot be deleted
     */
    void deleteSet(Long setId);
}