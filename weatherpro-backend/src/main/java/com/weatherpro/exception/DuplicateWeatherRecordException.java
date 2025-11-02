package com.weatherpro.exception;

/**
 * Exception thrown when attempting to create a duplicate weather record
 * (same location and overlapping date range)
 */
public class DuplicateWeatherRecordException extends RuntimeException {
    
    public DuplicateWeatherRecordException(String locationName, String dateRange) {
        super(String.format(
            "A weather record for '%s' already exists for the date range %s. " +
            "Please choose a different location or date range.",
            locationName, dateRange
        ));
    }
}

