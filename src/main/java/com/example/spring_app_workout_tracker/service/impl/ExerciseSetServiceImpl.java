package com.example.spring_app_workout_tracker.service.impl;

import com.example.spring_app_workout_tracker.entity.workout.ExerciseSet;
import com.example.spring_app_workout_tracker.repository.workout.ExerciseSetRepository;
import com.example.spring_app_workout_tracker.service.ExerciseSetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class ExerciseSetServiceImpl implements ExerciseSetService {
    private final ExerciseSetRepository exerciseSetRepository;

    @Override
    @Transactional
    public ExerciseSet updateSet(Long id, Integer reps, BigDecimal weightKg, Integer restSeconds) {
        ExerciseSet exerciseSet = exerciseSetRepository.findById(id).orElseThrow();
        if (reps != null) exerciseSet.setRepetitions(reps);
        if (weightKg != null) exerciseSet.setWeightKg(weightKg);
        if (restSeconds != null) exerciseSet.setRestSeconds(restSeconds);
        return exerciseSetRepository.save(exerciseSet);
    }

    @Override
    @Transactional
    public void deleteSet(Long id) {
        exerciseSetRepository.deleteById(id);
    }
}
