package com.weatherpro.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for weather record responses
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeatherResponseDTO {

    private UUID id;
    private String locationName;
    private String locationType;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal temperature;
    private BigDecimal feelsLike;
    private Integer humidity;
    private String weatherCondition;
    private String weatherDescription;
    private BigDecimal windSpeed;
    private Integer pressure;
    private String icon;
    private Integer aqi; // Air Quality Index (1-5)
    private String aqiCategory; // Good, Fair, Moderate, Poor, Very Poor
    private String country;
    private String state;
    private String additionalInfo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
}

