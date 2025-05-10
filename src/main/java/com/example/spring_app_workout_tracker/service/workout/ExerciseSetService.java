package com.example.spring_app_workout_tracker.service.workout;

import com.example.spring_app_workout_tracker.dto.workout.SetRequest;
import com.example.spring_app_workout_tracker.entity.workout.ExerciseSet;

import java.math.BigDecimal;

public interface ExerciseSetService {
    ExerciseSet createSet(Long workoutExerciseId, SetRequest request);

    ExerciseSet updateSet(Long setId, Integer repetitions, BigDecimal weightKg, Integer restSeconds);

    void deleteSet(Long setId);
}
