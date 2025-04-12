package com.example.spring_app_workout_tracker.repository;

import com.example.spring_app_workout_tracker.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}
