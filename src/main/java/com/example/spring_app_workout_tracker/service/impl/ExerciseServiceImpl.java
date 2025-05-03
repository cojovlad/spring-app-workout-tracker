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
        Exercise e = exerciseRepository.findById(id).orElseThrow();
        e.setName(name);
        return exerciseRepository.save(e);
    }

    @Override
    @Transactional
    public Exercise updateExercise(Long id, String name, String description) {
        Exercise e = exerciseRepository.findById(id).orElseThrow();
        e.setName(name);
        e.setDescription(description);
        return exerciseRepository.save(e);
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
        Exercise e = exerciseRepository.findById(exerciseId).orElseThrow();
        MusclePart m = musclePartRepository.findById(musclePartId).orElseThrow();
        if (!exerciseMuscleTargetRepository.existsByExerciseAndMusclePart(e, m)) {
            ExerciseMuscleTarget t = new ExerciseMuscleTarget();
            t.setId(new ExerciseMuscleTargetId(e.getId(), m.getId()));
            t.setExercise(e);
            t.setMusclePart(m);
            exerciseMuscleTargetRepository.save(t);
        }
    }

    @Override
    @Transactional
    public void removeMuscleTarget(Long exerciseId, Long musclePartId) {
        Exercise e = exerciseRepository.findById(exerciseId).orElseThrow();
        MusclePart m = musclePartRepository.findById(musclePartId).orElseThrow();
        exerciseMuscleTargetRepository.deleteByExerciseAndMusclePart(e, m);
    }

    @Override
    @Transactional
    public void updateExerciseMuscle(Long exerciseId, Long newMusclePartId) {
        Exercise e = exerciseRepository.findById(exerciseId).orElseThrow();
        MusclePart m = musclePartRepository.findById(newMusclePartId).orElseThrow();

        List<ExerciseMuscleTarget> old = exerciseMuscleTargetRepository.findByExercise(e);
        exerciseMuscleTargetRepository.deleteAll(old);

        ExerciseMuscleTarget t = new ExerciseMuscleTarget();
        t.setId(new ExerciseMuscleTargetId(e.getId(), m.getId()));
        t.setExercise(e);
        t.setMusclePart(m);
        exerciseMuscleTargetRepository.save(t);
    }
}
