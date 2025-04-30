package com.example.spring_app_workout_tracker.entity.workout;

import com.example.spring_app_workout_tracker.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "workouts",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"name", "created_by_user_id"}
        )
)
public class Workout {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Lob
    private String description;

    public enum Type { TEMPLATE, INSTANCE }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Type type = Type.TEMPLATE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_user_id", nullable = false)
    private User createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_template_id")
    @ToString.Exclude
    private Workout parentTemplate;

    @OneToMany(mappedBy = "parentTemplate", fetch = FetchType.LAZY)
    private List<Workout> childWorkouts = new ArrayList<>();

    @OneToMany(mappedBy = "workout", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkoutExercise> workoutExercises = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;
}