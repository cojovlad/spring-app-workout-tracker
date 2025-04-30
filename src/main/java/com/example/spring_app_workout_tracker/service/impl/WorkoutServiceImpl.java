package com.example.spring_app_workout_tracker.service.impl;

import com.example.spring_app_workout_tracker.dto.workout.ExerciseRequest;
import com.example.spring_app_workout_tracker.dto.workout.MuscleGroupRequest;
import com.example.spring_app_workout_tracker.dto.workout.SetRequest;
import com.example.spring_app_workout_tracker.dto.workout.WorkoutRequest;
import com.example.spring_app_workout_tracker.entity.*;
import com.example.spring_app_workout_tracker.entity.workout.*;
import com.example.spring_app_workout_tracker.exception.ExerciseNotFoundException;
import com.example.spring_app_workout_tracker.exception.ExerciseNotTargetingMuscleException;
import com.example.spring_app_workout_tracker.exception.MusclePartNotFoundException;
import com.example.spring_app_workout_tracker.repository.workout.*;
import com.example.spring_app_workout_tracker.service.WorkoutService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
@Transactional
public class WorkoutServiceImpl implements WorkoutService {

    private final WorkoutRepository workoutRepository;
    private final MusclePartRepository musclePartRepository;
    private final ExerciseRepository exerciseRepository;
    private final ExerciseMuscleTargetRepository exerciseMuscleTargetRepository;
    private final WorkoutExerciseRepository workoutExerciseRepository;
    private final ExerciseSetRepository exerciseSetRepository;

    public Workout createWorkoutTemplate(WorkoutRequest request, User user) {
        // Create and persist the workout template
        Workout workout = new Workout();
        workout.setName(request.getName());
        workout.setDescription(request.getDescription());
        workout.setType(Workout.Type.TEMPLATE);
        workout.setCreatedBy(user);
        workout = workoutRepository.save(workout);

        AtomicInteger globalSortOrder = new AtomicInteger(1);  // Maintain global ordering across all muscle groups

        for (MuscleGroupRequest muscleGroup : request.getMuscleGroups()) {
            processMuscleGroup(workout, muscleGroup, user, globalSortOrder);
        }

        return workoutRepository.findByIdWithDetails(workout.getId())
                .orElse(workout); // Handle fetch if needed
    }

    private void processMuscleGroup(Workout workout, MuscleGroupRequest muscleGroup,
                                    User user, AtomicInteger globalSortOrder) {
        // Resolve muscle part
        MusclePart musclePart = musclePartRepository.findByName(muscleGroup.getMuscleName())
                .orElseThrow(() -> new MusclePartNotFoundException(muscleGroup.getMuscleName()));

        // Process each exercise in order
        for (ExerciseRequest exerciseReq : muscleGroup.getExercises()) {
            processExercise(workout, musclePart, exerciseReq, user, globalSortOrder);
        }
    }

    private void processExercise(Workout workout, MusclePart musclePart,
                                 ExerciseRequest exerciseReq, User user, AtomicInteger globalSortOrder) {
        // Resolve exercise
        Exercise exercise = exerciseRepository.findByNameAndCreatedBy(exerciseReq.getExerciseName(), user)
                .orElseThrow(() -> new ExerciseNotFoundException(exerciseReq.getExerciseName()));

        // Validate exercise targets the muscle
        if (!exerciseMuscleTargetRepository.existsByExerciseAndMusclePart(exercise, musclePart)) {
            throw new ExerciseNotTargetingMuscleException(exercise.getName(), musclePart.getName());
        }

        // Create workout exercise
        WorkoutExercise workoutExercise = new WorkoutExercise();
        workoutExercise.setWorkout(workout);
        workoutExercise.setExercise(exercise);
        workoutExercise.setMusclePart(musclePart);
        workoutExercise.setSortOrder(globalSortOrder.getAndIncrement());
        workoutExercise.setNotes(exerciseReq.getNotes());
        workoutExercise = workoutExerciseRepository.save(workoutExercise);

        // Process sets
        processSets(exerciseReq.getSets(), workoutExercise);
    }

    private void processSets(List<SetRequest> sets, WorkoutExercise workoutExercise) {
        List<ExerciseSet> exerciseSets = new ArrayList<>();
        int setNumber = 1;

        for (SetRequest setReq : sets) {
            ExerciseSet set = new ExerciseSet();
            set.setWorkoutExercise(workoutExercise);
            set.setSetNumber(setNumber++);
            set.setRepetitions(setReq.getRepetitions());
            set.setWeightKg(setReq.getWeightKg());
            set.setRestSeconds(setReq.getRestSeconds());
            exerciseSets.add(set);
        }

        exerciseSetRepository.saveAll(exerciseSets);
    }

    public List<Workout> getWorkoutsByUser(User user) {
        return workoutRepository.findByCreatedByAndType(user, Workout.Type.TEMPLATE);
    }
}