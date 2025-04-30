package com.example.spring_app_workout_tracker.entity.workout;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "workout_exercises",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"workout_id", "sort_order"}
        )
)
public class WorkoutExercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workout_id", nullable = false)
    @EqualsAndHashCode.Exclude
    private Workout workout;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_id", nullable = false)
    private Exercise exercise;

    @Column(name = "sort_order", nullable = false)
    @Min(1) @Max(100)
    private Integer sortOrder;

    @ManyToOne
    @JoinColumn(name = "muscle_part_id", nullable = false)
    private MusclePart musclePart;

    @Lob
    private String notes;
}