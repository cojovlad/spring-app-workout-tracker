package com.example.spring_app_workout_tracker.dto.workout;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.util.List;

/**
 * Data Transfer Object (DTO) used to represent the details of an exercise, including
 * its name, a list of associated sets, and optional notes.
 * <p>
 * Validation annotations are applied to ensure proper input formatting and constraints.
 */
@Data
public class ExerciseRequest {

    /**
     * The name of the exercise.
     * <p>
     * This field must not be blank and must not exceed 100 characters.
     */
    @NotBlank(message = "Exercise name is required")
    @Size(max = 100, message = "Exercise name must be less than 100 characters")
    private String exerciseName;

    /**
     * A list of sets for the exercise.
     * <p>
     * This list must not be empty, and each {@link SetRequest} must be valid.
     */
    @NotEmpty(message = "At least one set is required")
    private List<@Valid SetRequest> sets;

    /**
     * Optional notes for the exercise.
     * <p>
     * If provided, the notes must not exceed 500 characters.
     */
    @Size(max = 500, message = "Notes must be less than 500 characters")
    private String notes;
}