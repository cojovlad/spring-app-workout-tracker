package com.example.spring_app_workout_tracker.dto.workout;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Transfer Object (DTO) representing the structure of a workout creation or update request.
 * <p>
 * This includes the workout's name, optional description, and a list of muscle groups
 * each containing exercises and their respective sets.
 * <p>
 * Validation is enforced using Jakarta Bean Validation annotations to ensure
 * the correctness and completeness of submitted data.
 */
@Data
public class WorkoutRequest {

    /**
     * The name of the workout.
     * <p>
     * Must not be blank and can be up to 100 characters long.
     */
    @NotBlank(message = "Workout name is required")
    @Size(max = 100, message = "Workout name must be less than 100 characters")
    private String name;

    /**
     * Optional description of the workout.
     * <p>
     * Can be up to 500 characters long.
     */
    @Size(max = 500, message = "Description must be less than 500 characters")
    private String description;

    /**
     * A list of muscle groups involved in the workout.
     * <p>
     * Each muscle group must contain at least one exercise.
     * This list must not be empty.
     */
    @Valid
    @NotEmpty(message = "At least one muscle group required")
    private List<MuscleGroupRequest> muscleGroups = new ArrayList<>();
}