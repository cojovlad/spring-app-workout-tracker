// MuscleGroupRequest.java
package com.example.spring_app_workout_tracker.dto.workout;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.util.List;

@Data
public class MuscleGroupRequest {
    @NotBlank @Size(max = 50)
    private String muscleName;

    @NotEmpty
    private List<ExerciseRequest> exercises;
}