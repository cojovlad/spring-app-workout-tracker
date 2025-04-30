package com.example.spring_app_workout_tracker.entity.workout;

import com.example.spring_app_workout_tracker.dto.ExerciseMuscleTargetId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "exercise_muscle_targets")
public class ExerciseMuscleTarget {
    @EmbeddedId
    private ExerciseMuscleTargetId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("exerciseId")
    @JoinColumn(name = "exercise_id", nullable = false)
    private Exercise exercise;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("musclePartId")
    @JoinColumn(name = "muscle_part_id", nullable = false)
    private MusclePart musclePart;
}