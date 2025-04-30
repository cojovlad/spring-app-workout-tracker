package com.example.spring_app_workout_tracker.repository.workout;

import com.example.spring_app_workout_tracker.entity.User;
import com.example.spring_app_workout_tracker.entity.workout.Workout;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkoutRepository extends JpaRepository<Workout, Long> {
    List<Workout> findByCreatedBy(User user);
    List<Workout> findByParentTemplate(Workout template);
    boolean existsByNameAndCreatedBy(String name, User createdBy);
    @EntityGraph(attributePaths = {
            "workoutExercises",
            "workoutExercises.exercise",
            "workoutExercises.exerciseSets"
    })
    @Query("SELECT w FROM Workout w WHERE w.id = :id")
    Optional<Workout> findByIdWithDetails(@Param("id") Long id);
    List<Workout> findByCreatedByAndType(User createdBy, Workout.Type type);
}