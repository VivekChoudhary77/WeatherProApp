package com.weatherpro.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO for weather record creation requests
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeatherRequestDTO {

    @NotBlank(message = "Location is required")
    @Size(min = 2, max = 200, message = "Location must be between 2 and 200 characters")
    private String location;

    @Size(max = 50, message = "Location type cannot exceed 50 characters")
    private String locationType; // zip, coordinates, city, landmark

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    private LocalDate endDate;

    @Size(max = 100, message = "Created by field cannot exceed 100 characters")
    private String createdBy;
}

