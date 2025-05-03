package com.example.spring_app_workout_tracker.service;

import com.example.spring_app_workout_tracker.entity.workout.MusclePart;

import java.util.List;

public interface MusclePartService {
    List<MusclePart> listAllMuscleParts();

    void updateMusclePart(Long id, Long muscleGroupId);
    }
