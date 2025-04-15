package com.example.spring_app_workout_tracker.service;

import com.example.spring_app_workout_tracker.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User createUser(User user);

    List<User> getAllUsers();

    User getUserById(Long id);

    User getUserByUsername(String username);

    User getUserByEmail(String email);

    void deleteUser(Long id);
}
