package com.example.spring_app_workout_tracker.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Custom implementation of {@link UserDetailsService} that
 * retrieves user details from the application's {@link UserService}.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;

    /**
     * Constructs a new {@code CustomUserDetailsService} with the given {@link UserService}.
     *
     * @param userService the user service used to load user data
     */
    public CustomUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    /**
     * Locates the user based on the username.
     *
     * @param username the username identifying the user whose data is required.
     * @return a fully populated {@link UserDetails} instance.
     * @throws UsernameNotFoundException if the user could not be found.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userService.getUserByUsername(username);
    }
}