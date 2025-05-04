package com.example.spring_app_workout_tracker.service.workout;

import com.example.spring_app_workout_tracker.dto.workout.SetRequest;
import com.example.spring_app_workout_tracker.entity.User;
import com.example.spring_app_workout_tracker.entity.workout.Exercise;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ExerciseService {
    Exercise createExerciseForWorkout(Long workoutId, String exerciseName, Long musclePartId, List<SetRequest> sets, User user);

    void addMuscleTarget(Long exerciseId, Long musclePartId);

    Exercise updateExerciseName(Long id, String name);

    void updateExerciseMuscle(Long exerciseId, Long newMusclePartId);

    void deleteExercise(Long id);

    void removeMuscleTarget(Long exerciseId, Long musclePartId);
}