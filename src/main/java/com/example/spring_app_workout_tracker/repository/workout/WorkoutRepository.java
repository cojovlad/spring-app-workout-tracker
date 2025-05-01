package com.example.spring_app_workout_tracker.repository.workout;

import com.example.spring_app_workout_tracker.entity.User;
import com.example.spring_app_workout_tracker.entity.workout.Workout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkoutRepository extends JpaRepository<Workout, Long> {
    List<Workout> findByCreatedBy(User user);
    List<Workout> findByParentTemplate(Workout template);
    boolean existsByNameAndCreatedBy(String name, User createdBy);
    @Query("SELECT DISTINCT w FROM Workout w " +
            "LEFT JOIN FETCH w.workoutExercises we " +
            "LEFT JOIN FETCH we.exercise " +
            "LEFT JOIN FETCH we.musclePart " +
            "LEFT JOIN FETCH we.exerciseSets " +
            "WHERE w.id = :id")
    Optional<Workout> findByIdWithDetailsById(Long id);
    List<Workout> findByCreatedByAndType(User createdBy, Workout.Type type);
}