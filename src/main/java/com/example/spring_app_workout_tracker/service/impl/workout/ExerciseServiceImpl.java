package com.example.spring_app_workout_tracker.service.impl.workout;

import com.example.spring_app_workout_tracker.dto.workout.SetRequest;
import com.example.spring_app_workout_tracker.entity.User;
import com.example.spring_app_workout_tracker.entity.workout.*;
import com.example.spring_app_workout_tracker.exception.workout.ExerciseNotFoundException;
import com.example.spring_app_workout_tracker.exception.workout.MusclePartNotFoundException;
import com.example.spring_app_workout_tracker.exception.workout.WorkoutExerciseNotFound;
import com.example.spring_app_workout_tracker.exception.workout.WorkoutNotFoundException;
import com.example.spring_app_workout_tracker.repository.workout.*;
import com.example.spring_app_workout_tracker.service.workout.ExerciseService;
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
    private final WorkoutRepository workoutRepository;

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
        Exercise exercise = exerciseRepository.findById(id)
                .orElseThrow(() -> new ExerciseNotFoundException(id.toString()));

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
        WorkoutExercise workoutExercise = workoutExerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new WorkoutExerciseNotFound(exerciseId.toString()));
        MusclePart musclePart = musclePartRepository.findById(newMusclePartId)
                .orElseThrow(() -> new MusclePartNotFoundException(newMusclePartId.toString()));
        workoutExercise.setMusclePart(musclePart);
        workoutExerciseRepository.save(workoutExercise);
    }

    @Override
    @Transactional
    public Exercise createExerciseForWorkout(Long workoutId, String exerciseName, Long musclePartId, List<SetRequest> sets, User user) {
        Workout workout = workoutRepository.findById(workoutId)
                .orElseThrow(() -> new WorkoutNotFoundException(workoutId));

        MusclePart musclePart = musclePartRepository.findById(musclePartId)
                .orElseThrow(() -> new MusclePartNotFoundException(musclePartId.toString()));

        Exercise exercise = new Exercise();
        exercise.setName(exerciseName);
        exercise.setCreatedBy(user);
        exercise = exerciseRepository.save(exercise);

        ExerciseMuscleTarget target = new ExerciseMuscleTarget();
        target.setId(new ExerciseMuscleTargetId());
        target.setExercise(exercise);
        target.setMusclePart(musclePart);
        exerciseMuscleTargetRepository.save(target);

        WorkoutExercise workoutExercise = new WorkoutExercise();
        workoutExercise.setWorkout(workout);
        workoutExercise.setExercise(exercise);
        workoutExercise.setMusclePart(musclePart);
        workoutExercise.setSortOrder(workoutExerciseRepository.findMaxSortOrder(workoutId) == null ? 1 : workoutExerciseRepository.findMaxSortOrder(workoutId) + 1);
        workoutExercise = workoutExerciseRepository.save(workoutExercise);

        if (sets != null) {
            int setNumber = 1;
            for (SetRequest setRequest : sets) {
                ExerciseSet exerciseSet = new ExerciseSet();
                exerciseSet.setWorkoutExercise(workoutExercise);
                exerciseSet.setSetNumber(setNumber++);
                exerciseSet.setRepetitions(setRequest.getRepetitions());
                exerciseSet.setWeightKg(setRequest.getWeightKg());
                exerciseSet.setRestSeconds(setRequest.getRestSeconds());
                exerciseSetRepository.save(exerciseSet);
            }
        }

        return exercise;
    }

    @Override
    @Transactional
    public void removeMuscleTarget(Long exerciseId, Long musclePartId) {
        Exercise exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new ExerciseNotFoundException(exerciseId.toString()));
        MusclePart musclePart = musclePartRepository.findById(musclePartId)
                .orElseThrow(() -> new MusclePartNotFoundException(musclePartId.toString()));
        exerciseMuscleTargetRepository.deleteByExerciseAndMusclePart(exercise, musclePart);
    }
}
