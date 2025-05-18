package com.example.spring_app_workout_tracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO representing a user's request to change their password.
 * <p>
 * This class is used for form binding and validation when a user submits a request
 * to update their password.
 */
@Data
public class PasswordChangeForm {

    /**
     * The user's current password.
     * <p>
     * This is required to verify the user's identity before allowing a password change.
     */
    @NotBlank(message = "Current password is required")
    private String currentPassword;

    /**
     * The new password the user wants to set.
     * <p>
     * Must meet minimum length requirements to ensure security.
     */
    @NotBlank(message = "New password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String newPassword;

    /**
     * A confirmation of the new password to help prevent typos.
     * <p>
     * This value should match {@code newPassword} before proceeding with the update.
     */
    @NotBlank(message = "Confirm password is required")
    private String confirmPassword;
}