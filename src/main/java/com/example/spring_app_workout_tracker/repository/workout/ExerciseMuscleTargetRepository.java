package com.example.spring_app_workout_tracker.repository.workout;

import com.example.spring_app_workout_tracker.entity.workout.ExerciseMuscleTargetId;
import com.example.spring_app_workout_tracker.entity.workout.Exercise;
import com.example.spring_app_workout_tracker.entity.workout.ExerciseMuscleTarget;
import com.example.spring_app_workout_tracker.entity.workout.MusclePart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExerciseMuscleTargetRepository extends JpaRepository<ExerciseMuscleTarget, ExerciseMuscleTargetId> {
    List<ExerciseMuscleTarget> findByExercise(Exercise exercise);
    List<ExerciseMuscleTarget> findByMusclePart(MusclePart musclePart);
    void deleteByExerciseAndMusclePart(Exercise exercise, MusclePart musclePart);
    @Query("SELECT COUNT(emt) > 0 FROM ExerciseMuscleTarget emt WHERE emt.exercise = :exercise AND emt.musclePart = :musclePart")
    boolean existsByExerciseAndMusclePart(@Param("exercise") Exercise exercise,
                                          @Param("musclePart") MusclePart musclePart);
    void deleteByExercise(Exercise exercise);
}