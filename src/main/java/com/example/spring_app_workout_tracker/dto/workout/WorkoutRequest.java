package com.example.spring_app_workout_tracker.dto.workout;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class WorkoutRequest {
    @NotBlank(message = "Workout name is required")
    @Size(max = 100, message = "Workout name must be less than 100 characters")
    private String name;

    @Size(max = 500, message = "Description must be less than 500 characters")
    private String description;

    @Valid
    @NotEmpty(message = "At least one muscle group required")
    private List<MuscleGroupRequest> muscleGroups = new ArrayList<>();
}