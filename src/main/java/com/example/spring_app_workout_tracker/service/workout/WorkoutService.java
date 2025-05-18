package com.example.spring_app_workout_tracker.service.workout;

import com.example.spring_app_workout_tracker.dto.workout.WorkoutRequest;
import com.example.spring_app_workout_tracker.entity.User;
import com.example.spring_app_workout_tracker.entity.workout.Workout;

import java.util.List;

/**
 * Service interface for managing {@link Workout} entities.
 * Provides methods to create, retrieve, update, and delete workouts.
 */
public interface WorkoutService {

    /**
     * Creates a new workout template for the specified user based on the given request data.
     *
     * @param request the {@link WorkoutRequest} containing workout details
     * @param user    the {@link User} who owns the workout
     * @return the created {@link Workout} entity
     */
    Workout createWorkoutTemplate(WorkoutRequest request, User user);

    /**
     * Retrieves all workouts associated with the given user.
     *
     * @param user the {@link User} whose workouts are to be retrieved
     * @return a list of {@link Workout} entities
     */
    List<Workout> getWorkoutsByUser(User user);

    /**
     * Retrieves a workout by its unique identifier.
     *
     * @param id the ID of the workout
     * @return the corresponding {@link Workout} entity
     * @throws IllegalArgumentException if no workout with the given ID is found
     */
    Workout getWorkoutById(Long id);

    /**
     * Deletes the workout identified by the given ID.
     *
     * @param id the ID of the workout to delete
     */
    void deleteWorkout(Long id);

    /**
     * Updates the name or description of the workout identified by the given ID.
     *
     * @param id      the ID of the workout to update
     * @param request the new name or description (depending on implementation)
     */
    void updateWorkout(Long id, String request);
}