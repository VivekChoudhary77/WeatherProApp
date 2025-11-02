package com.weatherpro.exception;

/**
 * Exception thrown when location validation fails
 */
public class InvalidLocationException extends RuntimeException {
    
    public InvalidLocationException(String message) {
        super(message);
    }
    
    public InvalidLocationException(String message, Throwable cause) {
        super(message, cause);
    }
}

