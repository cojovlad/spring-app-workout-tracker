package com.example.spring_app_workout_tracker.service.impl;

import com.example.spring_app_workout_tracker.entity.workout.*;
import com.example.spring_app_workout_tracker.repository.workout.*;
import com.example.spring_app_workout_tracker.service.ExerciseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExerciseServiceImpl implements ExerciseService {

    private final ExerciseRepository exerciseRepository;
    private final ExerciseMuscleTargetRepository exerciseMuscleTargetRepository;
    private final MusclePartRepository musclePartRepository;
    private final WorkoutExerciseRepository workoutExerciseRepository;
    private final ExerciseSetRepository exerciseSetRepository;

    @Override
    @Transactional
    public Exercise updateExerciseName(Long id, String name) {
        Exercise exercise = exerciseRepository.findById(id).orElseThrow();
        exercise.setName(name);
        return exerciseRepository.save(exercise);
    }

    @Override
    @Transactional
    public void deleteExercise(Long id) {
        Exercise exercise = exerciseRepository.findById(id).orElseThrow();

        List<WorkoutExercise> workoutExercises = workoutExerciseRepository.findByExerciseId(id);
        workoutExercises.forEach(exerciseSetRepository::deleteByWorkoutExercise);
        workoutExerciseRepository.deleteAll(workoutExercises);

        exerciseMuscleTargetRepository.deleteByExercise(exercise);

        exerciseRepository.delete(exercise);
    }

    @Override
    @Transactional
    public void addMuscleTarget(Long exerciseId, Long musclePartId) {
        Exercise exercise = exerciseRepository.findById(exerciseId).orElseThrow();
        MusclePart m = musclePartRepository.findById(musclePartId).orElseThrow();
        if (!exerciseMuscleTargetRepository.existsByExerciseAndMusclePart(exercise, m)) {
            ExerciseMuscleTarget exerciseMuscleTarget = new ExerciseMuscleTarget();
            exerciseMuscleTarget.setId(new ExerciseMuscleTargetId(exercise.getId(), m.getId()));
            exerciseMuscleTarget.setExercise(exercise);
            exerciseMuscleTarget.setMusclePart(m);
            exerciseMuscleTargetRepository.save(exerciseMuscleTarget);
        }
    }

    @Override
    @Transactional
    public void updateExerciseMuscle(Long exerciseId, Long newMusclePartId) {
        WorkoutExercise we = workoutExerciseRepository.findById(exerciseId).orElseThrow();
        MusclePart m = musclePartRepository.findById(newMusclePartId).orElseThrow();
        we.setMusclePart(m);
        workoutExerciseRepository.save(we);
    }

    @Override
    @Transactional
    public void removeMuscleTarget(Long exerciseId, Long musclePartId) {
        Exercise e = exerciseRepository.findById(exerciseId).orElseThrow();
        MusclePart m = musclePartRepository.findById(musclePartId).orElseThrow();
        exerciseMuscleTargetRepository.deleteByExerciseAndMusclePart(e, m);
    }
}
