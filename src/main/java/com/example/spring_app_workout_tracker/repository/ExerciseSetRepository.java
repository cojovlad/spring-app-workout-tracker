package com.example.spring_app_workout_tracker.repository;

import com.example.spring_app_workout_tracker.entity.ExerciseSet;
import com.example.spring_app_workout_tracker.entity.WorkoutExercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExerciseSetRepository extends JpaRepository<ExerciseSet, Long> {
    List<ExerciseSet> findByWorkoutExercise(WorkoutExercise workoutExercise);
    void deleteByWorkoutExercise(WorkoutExercise workoutExercise);
}