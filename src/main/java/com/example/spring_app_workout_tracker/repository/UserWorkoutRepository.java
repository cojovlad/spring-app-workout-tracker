package com.example.spring_app_workout_tracker.repository;

import com.example.spring_app_workout_tracker.entity.User;
import com.example.spring_app_workout_tracker.entity.UserWorkout;
import com.example.spring_app_workout_tracker.entity.Workout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface UserWorkoutRepository extends JpaRepository<UserWorkout, Long> {
    List<UserWorkout> findByUserAndScheduledDateBetween(User user, LocalDate start, LocalDate end);
    List<UserWorkout> findByWorkoutId(Long workoutId);
    boolean existsByUserAndWorkoutAndScheduledDate(User user, Workout workout, LocalDate date);
}