package com.example.spring_app_workout_tracker.dto.workout;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class AddExerciseRequest {
    @NotBlank(message = "Exercise name is required")
    @Size(max = 100, message = "Exercise name must be less than 100 characters")
    private String exerciseName;
    @NotBlank(message = "Muscle group selection is required")
    private Long musclePartId;
    @NotEmpty(message = "At least one set is required")
    private List<SetRequest> sets;
}
