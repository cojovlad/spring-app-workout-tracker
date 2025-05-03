package com.example.spring_app_workout_tracker.entity.workout;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"workoutExercise"})
@EqualsAndHashCode(exclude = {"workoutExercise"})
@Table(name = "exercise_sets")
public class ExerciseSet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "set_number", nullable = false)
    @Min(1)
    @Max(20)
    private Integer setNumber;

    @Column(nullable = false)
    @Min(1)
    @Max(100)
    private Integer repetitions;

    @Column(name = "weight_kg", nullable = false, precision = 5, scale = 2)
    @DecimalMin("0.25")
    @DecimalMax("500.00")
    private BigDecimal weightKg;

    @Column(name = "rest_seconds")
    @Min(0)
    @Max(600)
    private Integer restSeconds;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workout_exercise_id", nullable = false)
    @JsonIgnore
    private WorkoutExercise workoutExercise;
}