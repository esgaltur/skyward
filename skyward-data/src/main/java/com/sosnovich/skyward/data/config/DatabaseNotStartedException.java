package com.sosnovich.skyward.data.config;

/**
 * Exception thrown when the database is not started.
 */
public class DatabaseNotStartedException extends RuntimeException {

    /**
     * Constructs a new DatabaseNotStartedException with the specified detail message.
     *
     * @param message the detail message
     */
    public DatabaseNotStartedException(String message) {
        super(message);
    }

    /**
     * Constructs a new DatabaseNotStartedException with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause the cause of the exception
     */
    public DatabaseNotStartedException(String message, Throwable cause) {
        super(message, cause);
    }
}
