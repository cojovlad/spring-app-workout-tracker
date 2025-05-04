package com.example.spring_app_workout_tracker.service.workout;

import com.example.spring_app_workout_tracker.dto.workout.WorkoutRequest;
import com.example.spring_app_workout_tracker.entity.User;
import com.example.spring_app_workout_tracker.entity.workout.Workout;

import java.util.List;

public interface WorkoutService {
    Workout createWorkoutTemplate(WorkoutRequest request, User user);

    List<Workout> getWorkoutsByUser(User user);

    Workout getWorkoutById(Long id);

    void deleteWorkout(Long id);

    void updateWorkout(Long id, String request);
}
