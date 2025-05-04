package com.example.spring_app_workout_tracker.dto.workout;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class SetRequest {
    @NotNull(message = "Repetitions are required")
    @Min(value = 1, message = "Must do at least 1 repetition")
    @Max(value = 100, message = "Maximum 100 repetitions allowed")
    private Integer repetitions;

    @NotNull(message = "Weight is required")
    @DecimalMin(value = "0.00", message = "Minimum weight is 0.00kg")
    @DecimalMax(value = "500.00", message = "Maximum weight is 500kg")
    private BigDecimal weightKg;

    @Min(value = 0, message = "Rest time cannot be negative")
    @Max(value = 600, message = "Maximum rest time is 600 seconds")
    private Integer restSeconds = 0;
}