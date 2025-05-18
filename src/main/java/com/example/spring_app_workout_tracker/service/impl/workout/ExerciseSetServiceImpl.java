package com.example.spring_app_workout_tracker.service.impl.workout;

import com.example.spring_app_workout_tracker.dto.workout.SetRequest;
import com.example.spring_app_workout_tracker.entity.workout.ExerciseSet;
import com.example.spring_app_workout_tracker.entity.workout.WorkoutExercise;
import com.example.spring_app_workout_tracker.repository.workout.ExerciseSetRepository;
import com.example.spring_app_workout_tracker.repository.workout.WorkoutExerciseRepository;
import com.example.spring_app_workout_tracker.service.workout.ExerciseSetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class ExerciseSetServiceImpl implements ExerciseSetService {
    private final ExerciseSetRepository exerciseSetRepository;
    private final WorkoutExerciseRepository workoutExerciseRepository;

    @Override
    @Transactional
    public ExerciseSet createSet(Long workoutExerciseId, SetRequest request) {
        WorkoutExercise workoutExercise = workoutExerciseRepository.findById(workoutExerciseId)
                .orElseThrow(() -> new RuntimeException("Workout exercise not found"));

        int nextSetNumber;
        if(exerciseSetRepository.findMaxSetNumber(workoutExerciseId) == null){
            nextSetNumber=1;
        }
        else {
            nextSetNumber = exerciseSetRepository.findMaxSetNumber(workoutExerciseId) + 1;
        }

        ExerciseSet set = new ExerciseSet();
        set.setWorkoutExercise(workoutExercise);
        set.setSetNumber(nextSetNumber);
        set.setRepetitions(request.getRepetitions());
        set.setWeightKg(request.getWeightKg());
        set.setRestSeconds(request.getRestSeconds());

        return exerciseSetRepository.save(set);
    }

    @Override
    @Transactional
    public ExerciseSet updateSet(Long id, Integer reps, BigDecimal weightKg, Integer restSeconds) {
        ExerciseSet exerciseSet = exerciseSetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(id.toString()));
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
