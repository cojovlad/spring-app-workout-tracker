package com.example.spring_app_workout_tracker.repository;

import com.example.spring_app_workout_tracker.entity.Workout;
import com.example.spring_app_workout_tracker.entity.WorkoutExercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkoutExerciseRepository extends JpaRepository<WorkoutExercise, Long> {
    List<WorkoutExercise> findByWorkout(Workout workout);
    List<WorkoutExercise> findByExerciseId(Long exerciseId);
}