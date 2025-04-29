package com.example.spring_app_workout_tracker.repository;

import com.example.spring_app_workout_tracker.entity.MusclePart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MusclePartRepository extends JpaRepository<MusclePart, Long> {
    Optional<MusclePart> findByName(String name);
    boolean existsByName(String name);
}