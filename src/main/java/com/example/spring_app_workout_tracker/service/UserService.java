package com.example.spring_app_workout_tracker.service;

import com.example.spring_app_workout_tracker.entity.User;

import java.util.List;

/**
 * Service interface for managing {@link User} entities.
 */
public interface UserService {

    /**
     * Creates a new user.
     *
     * @param user the {@link User} entity to create
     * @return the created {@link User}
     */
    User createUser(User user);

    /**
     * Retrieves all users.
     *
     * @return a list of all {@link User} entities
     */
    List<User> getAllUsers();

    /**
     * Retrieves a user by their unique ID.
     *
     * @param id the user ID
     * @return the {@link User} with the given ID
     */
    User getUserById(Long id);

    /**
     * Retrieves a user by their username.
     *
     * @param username the username
     * @return the {@link User} with the given username
     */
    User getUserByUsername(String username);

    /**
     * Retrieves a user by their email address.
     *
     * @param email the email address
     * @return the {@link User} with the given email
     */
    User getUserByEmail(String email);

    /**
     * Deletes a user by their ID.
     *
     * @param id the user ID
     */
    void deleteUser(Long id);

    /**
     * Updates the language preference for a user identified by username.
     *
     * @param username the username
     * @param language the new language code or name
     */
    void updateUserLanguage(String username, String language);

    /**
     * Changes the password for a user.
     *
     * @param username the username
     * @param currentPassword the current password for verification
     * @param newPassword the new password to set
     */
    void changePassword(String username, String currentPassword, String newPassword);
}