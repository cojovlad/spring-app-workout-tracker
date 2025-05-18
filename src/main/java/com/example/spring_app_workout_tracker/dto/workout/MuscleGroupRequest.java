package com.example.spring_app_workout_tracker.dto.workout;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.util.List;

/**
 * Data Transfer Object (DTO) for representing a muscle group along with its associated exercises.
 * <p>
 * This class is typically used in scenarios where a workout contains multiple muscle groups,
 * and each muscle group includes one or more exercises.
 * <p>
 * Validation is applied to ensure proper structure and content.
 */
@Data
public class MuscleGroupRequest {

    /**
     * The name of the muscle group (e.g., "Chest", "Back", "Legs").
     * <p>
     * This field is required and must not be blank.
     */
    @NotBlank(message = "Muscle group selection is required")
    private String muscleName;

    /**
     * A list of exercises associated with the muscle group.
     * <p>
     * The list must contain at least one {@link ExerciseRequest}, and each must be valid.
     */
    @NotEmpty(message = "At least one exercise required per muscle group")
    private List<@Valid ExerciseRequest> exercises;
}