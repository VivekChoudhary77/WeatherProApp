package com.weatherpro.exception;

import java.util.UUID;

/**
 * Exception thrown when weather record is not found
 */
public class WeatherRecordNotFoundException extends RuntimeException {
    
    public WeatherRecordNotFoundException(UUID id) {
        super("Weather record not found with id: " + id);
    }
    
    public WeatherRecordNotFoundException(String message) {
        super(message);
    }
}

