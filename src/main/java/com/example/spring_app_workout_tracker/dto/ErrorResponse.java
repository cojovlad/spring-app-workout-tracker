package com.example.spring_app_workout_tracker.dto;

import lombok.Data;

/**
 * A standard structure for returning error details in API responses.
 * <p>
 * This class encapsulates a user-friendly error message and an application-specific error code,
 * which can be used for client-side handling or debugging.
 */
@Data
public class ErrorResponse {

    /**
     * A human-readable error message intended for end-user display.
     */
    private String message;

    /**
     * A short, application-defined code representing the type of error.
     * <p>
     * Useful for localization or client-side logic branching.
     */
    private String code;

    /**
     * Constructs a new {@code ErrorResponse} with the specified message and error code.
     *
     * @param message the human-readable error message
     * @param code    the application-specific error code
     */
    public ErrorResponse(String message, String code) {
        this.message = message;
        this.code = code;
    }
}