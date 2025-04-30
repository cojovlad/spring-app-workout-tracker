// WorkoutRequest.java
package com.example.spring_app_workout_tracker.dto.workout;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.util.List;

@Data
public class WorkoutRequest {
    @NotBlank @Size(max = 100)
    private String name;

    @Size(max = 500)
    private String description;

    @NotEmpty
    private List<MuscleGroupRequest> muscleGroups;
}