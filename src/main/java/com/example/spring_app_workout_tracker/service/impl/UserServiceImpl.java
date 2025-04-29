package com.example.spring_app_workout_tracker.service.impl;

import com.example.spring_app_workout_tracker.entity.Language;
import com.example.spring_app_workout_tracker.entity.Role;
import com.example.spring_app_workout_tracker.entity.User;
import com.example.spring_app_workout_tracker.exception.CustomAppException;
import com.example.spring_app_workout_tracker.repository.LanguageRepository;
import com.example.spring_app_workout_tracker.repository.RoleRepository;
import com.example.spring_app_workout_tracker.repository.UserRepository;
import com.example.spring_app_workout_tracker.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final LanguageRepository languageRepository;
    private final RoleRepository roleRepository;
    private final MessageSource messageSource;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, LanguageRepository languageRepository, RoleRepository roleRepository, MessageSource messageSource) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.languageRepository = languageRepository;
        this.roleRepository = roleRepository;
        this.messageSource = messageSource;
    }

    @Override
    @Transactional
    public User createUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new CustomAppException("error.username.taken", null, HttpStatus.BAD_REQUEST, "USERNAME_TAKEN", messageSource);
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new CustomAppException("error.email.taken", null, HttpStatus.BAD_REQUEST, "EMAIL_TAKEN", messageSource);
        }

        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            Role defaultRole = roleRepository.findById(1L)
                    .orElseThrow(() -> new CustomAppException("error.role.not.found", new Object[]{1L}, HttpStatus.INTERNAL_SERVER_ERROR, "DEFAULT_ROLE_NOT_FOUND", messageSource));
            user.getRoles().add(defaultRole);
        }

        if (user.getPreferredLanguage() == null) {
            Language defaultLanguage = languageRepository.findByCode("en")
                    .orElseThrow(() -> new CustomAppException("error.language.not.found", new Object[]{"en"}, HttpStatus.INTERNAL_SERVER_ERROR, "DEFAULT_LANGUAGE_NOT_FOUND", messageSource));
            user.setPreferredLanguage(defaultLanguage);
        }

        user.setPasswordHash(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            throw new CustomAppException("error.users.not.found", null, HttpStatus.NOT_FOUND, "NO_USERS_FOUND", messageSource);
        }
        return users;
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new CustomAppException("error.user.not.found", new Object[]{id}, HttpStatus.NOT_FOUND, "USER_NOT_FOUND", messageSource)
        );
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() ->
                new CustomAppException("error.user.not.found.username", new Object[]{username}, HttpStatus.NOT_FOUND, "USER_NOT_FOUND", messageSource)
        );
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() ->
                new CustomAppException("error.user.not.found.email", new Object[]{email}, HttpStatus.NOT_FOUND, "USER_NOT_FOUND", messageSource)
        );
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new CustomAppException("error.user.not.found", new Object[]{id}, HttpStatus.NOT_FOUND, "USER_NOT_FOUND", messageSource);
        }
        userRepository.deleteById(id);
    }

    public List<Language> getAllLanguages() {
        return languageRepository.findAll();
    }

    @Override
    public void updateUserLanguage(String username, String code) {
        User user = getUserByUsername(username);
        Language language = languageRepository.findByCode(code).orElseThrow(() -> new EntityNotFoundException("language.not.found"));

        user.setPreferredLanguage(language);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void changePassword(String username, String currentPassword, String newPassword) {
        User user = getUserByUsername(username);

        if (!passwordEncoder.matches(currentPassword, user.getPasswordHash())) {
            throw new CustomAppException("error.password.incorrect", null, HttpStatus.BAD_REQUEST, "INCORRECT_PASSWORD", messageSource);
        }

        if (currentPassword.equals(newPassword)) {
            throw new CustomAppException("error.password.same", null, HttpStatus.BAD_REQUEST, "SAME_PASSWORD", messageSource);
        }

        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}
