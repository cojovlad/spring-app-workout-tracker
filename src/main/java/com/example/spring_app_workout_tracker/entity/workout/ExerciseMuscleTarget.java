package com.example.spring_app_workout_tracker.entity.workout;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="exercise_muscle_targets")
public class ExerciseMuscleTarget {
    @EmbeddedId
    private ExerciseMuscleTargetId id;

    @ManyToOne
    @MapsId("exerciseId")
    @JoinColumn(name = "exercise_id")
    private Exercise exercise;

    @ManyToOne
    @MapsId("musclePartId")
    @JoinColumn(name = "muscle_part_id")
    private MusclePart musclePart;
}