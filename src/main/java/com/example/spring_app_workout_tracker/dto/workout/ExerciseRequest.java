package com.example.spring_app_workout_tracker.dto.workout;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.util.List;

@Data
public class ExerciseRequest {
    @NotBlank(message = "Exercise name is required")
    @Size(max = 100, message = "Exercise name must be less than 100 characters")
    private String exerciseName;

    @NotEmpty(message = "At least one set is required")
    private List<@Valid SetRequest> sets;

    @Size(max = 500, message = "Notes must be less than 500 characters")
    private String notes;
}