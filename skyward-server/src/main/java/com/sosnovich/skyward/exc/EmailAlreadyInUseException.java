package com.sosnovich.skyward.exc;

/**
 * Exception thrown when an attempt is made to register a user with an email that is already in use.
 */
public class EmailAlreadyInUseException extends RuntimeException {

    /**
     * Constructs a new EmailAlreadyInUseException with the specified detail message and cause.
     *
     * @param message the detail message
     */
    public EmailAlreadyInUseException(String message) {
        super(message);
    }
}
