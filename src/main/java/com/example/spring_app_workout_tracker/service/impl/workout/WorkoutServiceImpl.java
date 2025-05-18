package com.example.spring_app_workout_tracker.service.impl.workout;

import com.example.spring_app_workout_tracker.entity.workout.ExerciseMuscleTargetId;
import com.example.spring_app_workout_tracker.dto.workout.ExerciseRequest;
import com.example.spring_app_workout_tracker.dto.workout.MuscleGroupRequest;
import com.example.spring_app_workout_tracker.dto.workout.SetRequest;
import com.example.spring_app_workout_tracker.dto.workout.WorkoutRequest;
import com.example.spring_app_workout_tracker.entity.*;
import com.example.spring_app_workout_tracker.entity.workout.*;
import com.example.spring_app_workout_tracker.exception.workout.MusclePartNotFoundException;
import com.example.spring_app_workout_tracker.exception.workout.WorkoutNotFoundException;
import com.example.spring_app_workout_tracker.repository.workout.*;
import com.example.spring_app_workout_tracker.service.workout.WorkoutService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
    private final UserWorkoutRepository userWorkoutRepository;

    public Workout createWorkoutTemplate(WorkoutRequest request, User user) {
        Workout workout = new Workout();
        workout.setName(request.getName());
        workout.setDescription(request.getDescription());
        workout.setType(Workout.Type.TEMPLATE);
        workout.setCreatedBy(user);
        workout = workoutRepository.save(workout);

        AtomicInteger globalSortOrder = new AtomicInteger(1);

        for (MuscleGroupRequest muscleGroup : request.getMuscleGroups()) {
            processMuscleGroup(workout, muscleGroup, user, globalSortOrder);
        }

        UserWorkout userWorkout = new UserWorkout();
        userWorkout.setUser(user);
        userWorkout.setWorkout(workout);
        userWorkout.setScheduledDate(LocalDate.now());
        userWorkout.setStatus(UserWorkout.Status.PLANNED);

        userWorkoutRepository.save(userWorkout);

        return workoutRepository.findByIdWithDetailsById(workout.getId())
                .orElse(workout);
    }

    private void processMuscleGroup(Workout workout, MuscleGroupRequest muscleGroup,
                                    User user, AtomicInteger globalSortOrder) {
        MusclePart musclePart = musclePartRepository.findByName(muscleGroup.getMuscleName())
                .orElseThrow(() -> new MusclePartNotFoundException(muscleGroup.getMuscleName()));

        for (ExerciseRequest exerciseReq : muscleGroup.getExercises()) {
            processExercise(workout, musclePart, exerciseReq, user, globalSortOrder);
        }
    }

    private void processExercise(Workout workout, MusclePart musclePart,
                                 ExerciseRequest exerciseReq, User user, AtomicInteger globalSortOrder) {

        Exercise exercise = new Exercise();
        exercise.setName(exerciseReq.getExerciseName());
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
        workoutExercise.setSortOrder(globalSortOrder.getAndIncrement());
        workoutExercise.setNotes(exerciseReq.getNotes());
        workoutExercise = workoutExerciseRepository.save(workoutExercise);

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

    public Workout getWorkoutById(Long id) {
        return workoutRepository.findByIdWithDetailsById(id)
                .orElseThrow(() -> new WorkoutNotFoundException(id));
    }

    public void updateWorkout(Long id, String name) {
        Workout workout = workoutRepository.findById(id)
                .orElseThrow(() -> new WorkoutNotFoundException(id));
        workout.setName(name);
        workoutRepository.save(workout);
    }

    public void deleteWorkout(Long id) {
        Workout workout = workoutRepository.findById(id)
                .orElseThrow(() -> new WorkoutNotFoundException(id));
        workoutRepository.delete(workout);
    }
}