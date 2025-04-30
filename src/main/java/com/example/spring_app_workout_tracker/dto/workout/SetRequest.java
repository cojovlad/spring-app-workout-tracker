// SetRequest.java
package com.example.spring_app_workout_tracker.dto.workout;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class SetRequest {
    @NotNull @Min(1) @Max(100)
    private Integer repetitions;

    @NotNull @DecimalMin("0.25") @DecimalMax("500.00")
    private BigDecimal weightKg;

    @Min(0) @Max(600)
    private Integer restSeconds = 0;
}