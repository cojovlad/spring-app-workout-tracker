package com.example.spring_app_workout_tracker.service.workout;

import com.example.spring_app_workout_tracker.entity.workout.MusclePart;

import java.util.List;

/**
 * Service interface for managing {@link MusclePart} entities.
 * Provides methods to retrieve and update muscle parts.
 */
public interface MusclePartService {

    /**
     * Retrieves a list of all available muscle parts.
     *
     * @return a list of {@link MusclePart} objects
     */
    List<MusclePart> listAllMuscleParts();

    /**
     * Updates the muscle group association of a specific muscle part.
     *
     * @param id             the ID of the muscle part to update
     * @param muscleGroupId  the ID of the new muscle group to associate with the muscle part
     * @throws IllegalArgumentException if the muscle part or muscle group does not exist
     */
    void updateMusclePart(Long id, Long muscleGroupId);
}