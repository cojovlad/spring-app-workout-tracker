package com.example.spring_app_workout_tracker.service;

import com.example.spring_app_workout_tracker.entity.Language;

import java.util.List;

/**
 * Service interface for managing {@link Language} entities.
 */
public interface LanguageService {

    /**
     * Retrieves all available languages.
     *
     * @return a list of all {@link Language} entities
     */
    List<Language> getAllLanguages();
}