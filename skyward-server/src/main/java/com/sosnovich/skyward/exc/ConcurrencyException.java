package com.sosnovich.skyward.exc;

/**
 * Exception thrown when a concurrency conflict occurs, such as when attempting to modify
 * a resource that has been concurrently modified by another transaction.
 */
public class ConcurrencyException extends RuntimeException {

    /**
     * Constructs a new {@code ConcurrencyException} with the specified detail message and cause.
     *
     * @param message the detail message (which is saved for later retrieval by the {@link #getMessage()} method)
     * @param cause the cause (which is saved for later retrieval by the {@link #getCause()} method).
     *              (A {@code null} value is permitted, and indicates that the cause is nonexistent or unknown.)
     */
    public ConcurrencyException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new {@code ConcurrencyException} with the specified detail message.
     *
     * @param message the detail message (which is saved for later retrieval by the {@link #getMessage()} method)
     */
    public ConcurrencyException(String message) {
        super(message);
    }
}


