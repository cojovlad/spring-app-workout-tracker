package com.example.spring_app_workout_tracker.service.impl;

import com.example.spring_app_workout_tracker.entity.workout.MusclePart;
import com.example.spring_app_workout_tracker.entity.workout.WorkoutExercise;
import com.example.spring_app_workout_tracker.repository.workout.MusclePartRepository;
import com.example.spring_app_workout_tracker.repository.workout.WorkoutExerciseRepository;
import com.example.spring_app_workout_tracker.service.MusclePartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MusclePartServiceImpl implements MusclePartService {

    private final MusclePartRepository musclePartRepository;
    private final WorkoutExerciseRepository workoutExerciseRepository;

    @Autowired
    public MusclePartServiceImpl(MusclePartRepository musclePartRepository, WorkoutExerciseRepository workoutExerciseRepository) {
        this.musclePartRepository = musclePartRepository;
        this.workoutExerciseRepository = workoutExerciseRepository;
    }

    @Override
    public List<MusclePart> listAllMuscleParts() {
       return musclePartRepository.findAll();
    }

    @Override
    public void updateMusclePart(Long id, Long muscleGroupId) {
        //TODO see why WorkoutExercise has an error but it still works
        WorkoutExercise workoutExercise = workoutExerciseRepository.findById(id).orElseThrow();
        MusclePart musclePart = musclePartRepository.findById(muscleGroupId).orElseThrow();

        workoutExercise.setMusclePart(musclePart);
        workoutExerciseRepository.save(workoutExercise);
    }
}
