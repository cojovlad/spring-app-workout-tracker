package com.example.spring_app_workout_tracker.entity.workout;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"workout", "exerciseSets"})
@EqualsAndHashCode(exclude = {"workout"})
@Table(name = "workout_exercises",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"workout_id", "sort_order"}
        )
)
public class WorkoutExercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sort_order", nullable = false)
    @Min(1)
    @Max(100)
    private Integer sortOrder;

    @OneToMany(mappedBy = "workoutExercise", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("workoutExercise")
    private Set<ExerciseSet> exerciseSets = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workout_id", nullable = false)
    @JsonIgnore
    private Workout workout;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_id", nullable = false)
    private Exercise exercise;

    @ManyToOne
    @JoinColumn(name = "muscle_part_id", nullable = false)
    private MusclePart musclePart;

    @Column(columnDefinition = "TEXT")
    private String notes;
}