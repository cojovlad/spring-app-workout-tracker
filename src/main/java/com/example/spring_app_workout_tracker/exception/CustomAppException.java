package com.example.spring_app_workout_tracker.exception;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.context.i18n.LocaleContextHolder;

public class CustomAppException extends RuntimeException {
    private final String localizedMessage;
    private final String messageKey;
    private final Object[] args;
    private final HttpStatus status;
    private final String code;

    public CustomAppException(String messageKey, Object[] args, HttpStatus status, String code, MessageSource messageSource) {
        super(messageKey);
        this.localizedMessage = messageSource.getMessage(messageKey, args, LocaleContextHolder.getLocale());
        this.messageKey = messageKey;
        this.args = args;
        this.status = status;
        this.code = code;
    }

    @Override
    public String getMessage() {
        return localizedMessage;
    }

    public String getMessageKey() { return messageKey; }
    public Object[] getArgs() { return args; }
    public HttpStatus getStatus() { return status; }
    public String getCode() { return code; }
}
