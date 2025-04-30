package com.example.spring_app_workout_tracker.dto.workout;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.util.List;

@Data
public class ExerciseRequest {
    @NotBlank @Size(max = 100)
    private String exerciseName;

    @NotEmpty
    private List<SetRequest> sets;

    @Size(max = 500)
    private String notes;
}