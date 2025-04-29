package com.example.spring_app_workout_tracker.repository;

import com.example.spring_app_workout_tracker.entity.Exercise;
import com.example.spring_app_workout_tracker.entity.ExerciseMuscleTarget;
import com.example.spring_app_workout_tracker.entity.MusclePart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExerciseMuscleTargetRepository extends JpaRepository<ExerciseMuscleTarget, Long> {
    List<ExerciseMuscleTarget> findByExercise(Exercise exercise);
    List<ExerciseMuscleTarget> findByMusclePart(MusclePart musclePart);
    void deleteByExerciseAndMusclePart(Exercise exercise, MusclePart musclePart);
}