package com.sosnovich.skyward.service.errors;

public enum ServiceError {
    USER_NOT_FOUND("User not found with ID: %s"),
    EMAIL_ALREADY_IN_USE("Email [%s] already in use"),
    PROJECT_ALREADY_EXISTS("Project already exists: %s");

    private final String messageTemplate;

    ServiceError(String messageTemplate) {
        this.messageTemplate = messageTemplate;
    }

    public String format(Object... args) {
        return String.format(this.messageTemplate, args);
    }
}
