package com.example.spring_app_workout_tracker.exception;

public class RoleNotFoundException extends RuntimeException {
    public RoleNotFoundException(Long role) {
        super("The role"+role+"was not found");
    }
}
