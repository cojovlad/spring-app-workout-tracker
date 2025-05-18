package com.example.spring_app_workout_tracker.service.workout;

import com.example.spring_app_workout_tracker.dto.workout.SetRequest;
import com.example.spring_app_workout_tracker.entity.User;
import com.example.spring_app_workout_tracker.entity.workout.Exercise;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service interface for managing {@link Exercise} entities within a workout context.
 * Provides operations for creating, updating, deleting, and managing muscle targets.
 */
public interface ExerciseService {

    /**
     * Creates a new exercise and associates it with a given workout and muscle group.
     *
     * @param workoutId    the ID of the workout the exercise belongs to
     * @param exerciseName the name of the exercise
     * @param musclePartId the ID of the primary muscle group targeted
     * @param sets         the list of sets associated with the exercise
     * @param user         the user creating the exercise (used for ownership/authorization)
     * @return the created {@link Exercise} entity
     * @throws IllegalArgumentException if the workout or muscle group is invalid
     * @throws RuntimeException if the creation fails due to business constraints
     */
    Exercise createExerciseForWorkout(Long workoutId, String exerciseName, Long musclePartId, List<SetRequest> sets, User user);

    /**
     * Adds a muscle target to an existing exercise.
     *
     * @param exerciseId   the ID of the exercise
     * @param musclePartId the ID of the muscle part to associate
     * @throws IllegalArgumentException if the exercise or muscle part is invalid
     */
    void addMuscleTarget(Long exerciseId, Long musclePartId);

    /**
     * Updates the name of an existing exercise.
     *
     * @param id   the ID of the exercise
     * @param name the new name for the exercise
     * @return the updated {@link Exercise} entity
     * @throws IllegalArgumentException if the name is invalid
     */
    Exercise updateExerciseName(Long id, String name);

    /**
     * Updates the primary muscle group targeted by an exercise.
     *
     * @param exerciseId     the ID of the exercise
     * @param newMusclePartId the new muscle part ID
     * @throws IllegalArgumentException if the muscle part is invalid
     */
    void updateExerciseMuscle(Long exerciseId, Long newMusclePartId);

    /**
     * Deletes an exercise by its ID.
     *
     * @param id the ID of the exercise to delete
     * @throws IllegalArgumentException if the exercise ID is invalid
     */
    void deleteExercise(Long id);

    /**
     * Removes a muscle target from an exercise.
     *
     * @param exerciseId   the ID of the exercise
     * @param musclePartId the ID of the muscle part to remove
     * @throws IllegalArgumentException if the association does not exist
     */
    void removeMuscleTarget(Long exerciseId, Long musclePartId);
}