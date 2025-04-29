package com.example.spring_app_workout_tracker.repository;

import com.example.spring_app_workout_tracker.entity.Exercise;
import com.example.spring_app_workout_tracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    List<Exercise> findByCreatedBy(User user);
    boolean existsByNameAndCreatedBy(String name, User createdBy);
}