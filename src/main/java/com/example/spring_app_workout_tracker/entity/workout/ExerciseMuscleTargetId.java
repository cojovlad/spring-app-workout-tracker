package com.example.spring_app_workout_tracker.entity.workout;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseMuscleTargetId implements java.io.Serializable {
    private Long exerciseId;
    private Long musclePartId;

    public void setExerciseId(Long exerciseId) {
        this.exerciseId = exerciseId;
    }

    public void setMusclePartId(Long musclePartId) {
        this.musclePartId = musclePartId;
    }
}
