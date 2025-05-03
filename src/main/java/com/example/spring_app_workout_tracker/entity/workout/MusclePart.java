package com.example.spring_app_workout_tracker.entity.workout;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "muscle_parts",
        uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class MusclePart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String name;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Date createdAt;
}