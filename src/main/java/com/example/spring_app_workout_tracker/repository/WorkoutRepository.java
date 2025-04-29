package com.example.spring_app_workout_tracker.repository;

import com.example.spring_app_workout_tracker.entity.User;
import com.example.spring_app_workout_tracker.entity.Workout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkoutRepository extends JpaRepository<Workout, Long> {
    List<Workout> findByCreatedBy(User user);
    List<Workout> findByParentTemplate(Workout template);
    boolean existsByNameAndCreatedBy(String name, User createdBy);
}