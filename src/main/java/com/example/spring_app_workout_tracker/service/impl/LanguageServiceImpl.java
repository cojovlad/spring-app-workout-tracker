package com.example.spring_app_workout_tracker.service.impl;

import com.example.spring_app_workout_tracker.entity.Language;
import com.example.spring_app_workout_tracker.repository.LanguageRepository;
import com.example.spring_app_workout_tracker.service.LanguageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LanguageServiceImpl implements LanguageService {

    private final LanguageRepository languageRepository;

    @Autowired
    public LanguageServiceImpl(LanguageRepository languageRepository) {
        this.languageRepository = languageRepository;
    }

    public List<Language> getAllLanguages() {
        return languageRepository.findAll();
    }
}
