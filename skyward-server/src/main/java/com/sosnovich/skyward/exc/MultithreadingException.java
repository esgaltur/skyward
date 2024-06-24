package com.sosnovich.skyward.exc;

/**
 * Exception thrown when an error occurs in multithreading operations.
 */
public class MultithreadingException extends RuntimeException {

    /**
     * Constructs a new MultithreadingException with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause the cause of the exception
     */
    public MultithreadingException(String message, Throwable cause) {
        super(message, cause);
    }
}
