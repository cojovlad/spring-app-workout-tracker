package com.example.spring_app_workout_tracker.dto.workout;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

/**
 * Data Transfer Object (DTO) representing the payload required to add a new exercise
 * to a workout. This includes the exercise name, targeted muscle group, and a list of sets.
 * <p>
 * Validation annotations are used to enforce constraints on the incoming data.
 */
@Data
public class AddExerciseRequest {

    /**
     * The name of the exercise to be added.
     * <p>
     * Must not be blank and must not exceed 100 characters in length.
     */
    @NotBlank(message = "Exercise name is required")
    @Size(max = 100, message = "Exercise name must be less than 100 characters")
    private String exerciseName;

    /**
     * The ID of the muscle group that this exercise targets.
     * <p>
     * Must not be null.
     */
    @NotNull(message = "Muscle group selection is required")
    private Long musclePartId;

    /**
     * A list of sets associated with this exercise.
     * <p>
     * Must contain at least one valid {@link SetRequest}.
     */
    @NotEmpty(message = "At least one set is required")
    private List<SetRequest> sets;
}
