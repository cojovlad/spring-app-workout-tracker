package com.example.spring_app_workout_tracker.dto.workout;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.util.List;

@Data
public class MuscleGroupRequest {
    @NotBlank(message = "Muscle group selection is required")
    private String muscleName;

    @NotEmpty(message = "At least one exercise required per muscle group")
    private List<ExerciseRequest> exercises;
}