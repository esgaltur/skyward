package com.sosnovich.skyward.exc;

/**
 * Exception thrown when attempting to create a project that already exists.
 */
public class ProjectAlreadyExistsException extends RuntimeException {

    /**
     * Constructs a new ProjectAlreadyExistsException with the specified detail message.
     *
     * @param message the detail message
     */
    public ProjectAlreadyExistsException(String message) {
        super(message);
    }
}
