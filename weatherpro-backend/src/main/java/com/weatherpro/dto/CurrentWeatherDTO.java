package com.weatherpro.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO for current weather information
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CurrentWeatherDTO {

    private String locationName;
    private BigDecimal latitude;
    private BigDecimal longitude;
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
    private Long sunrise;
    private Long sunset;
    private Integer visibility;
    private Integer clouds;
    private String country;
}

