package com.example.spring_app_workout_tracker.dto;

import lombok.Data;

@Data
public class ErrorResponse {
    private String message;
    private String code;

    public ErrorResponse(String message, String code) {
        this.message = message;
        this.code = code;
    }
}
