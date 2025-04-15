package com.example.spring_app_workout_tracker.exception;

import org.springframework.http.HttpStatus;

public class CustomAppException extends RuntimeException {
    private final String messageKey;
    private final Object[] args;
    private final HttpStatus status;
    private final String code;

    public CustomAppException(String messageKey, Object[] args, HttpStatus status, String code) {
        super(messageKey);
        this.messageKey = messageKey;
        this.args = args;
        this.status = status;
        this.code = code;
    }

    public String getMessageKey() { return messageKey; }
    public Object[] getArgs() { return args; }
    public HttpStatus getStatus() { return status; }
    public String getCode() { return code; }
}

