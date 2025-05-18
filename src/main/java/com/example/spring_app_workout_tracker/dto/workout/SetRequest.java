package com.example.spring_app_workout_tracker.dto.workout;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

/**
 * Data Transfer Object (DTO) representing a single exercise set within a workout.
 * <p>
 * This class includes details such as the number of repetitions, the weight used,
 * and the rest period following the set.
 * <p>
 * It is typically nested within {@code ExerciseRequest} and validated via annotations.
 */
@Data
public class SetRequest {

    /**
     * The number of repetitions for the set.
     * <p>
     * Must be between 1 and 100 (inclusive).
     */
    @NotNull(message = "Repetitions are required")
    @Min(value = 1, message = "Must do at least 1 repetition")
    @Max(value = 100, message = "Maximum 100 repetitions allowed")
    private Integer repetitions;

    /**
     * The weight used for the set in kilograms.
     * <p>
     * Must be between 0.00kg and 500.00kg (inclusive).
     */
    @NotNull(message = "Weight is required")
    @DecimalMin(value = "0.00", message = "Minimum weight is 0.00kg")
    @DecimalMax(value = "500.00", message = "Maximum weight is 500kg")
    private BigDecimal weightKg;

    /**
     * Rest time after the set in seconds.
     * <p>
     * Optional. Defaults to 0 if not provided. Must be between 0 and 600 seconds.
     */
    @Min(value = 0, message = "Rest time cannot be negative")
    @Max(value = 600, message = "Maximum rest time is 600 seconds")
    private Integer restSeconds = 0;
}