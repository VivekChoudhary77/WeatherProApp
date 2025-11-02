package com.weatherpro.exception;

/**
 * Exception thrown when date range validation fails
 */
public class InvalidDateRangeException extends RuntimeException {
    
    public InvalidDateRangeException(String message) {
        super(message);
    }
}

