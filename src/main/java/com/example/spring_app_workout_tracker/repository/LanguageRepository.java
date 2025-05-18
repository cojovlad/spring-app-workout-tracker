package com.example.spring_app_workout_tracker.repository;

import com.example.spring_app_workout_tracker.entity.Language;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LanguageRepository extends JpaRepository<Language, Long> {

    Optional<Language> findByName(String name);

    Optional<Language> findByCode(String code);
}
