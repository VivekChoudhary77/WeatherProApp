package com.weatherpro.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * DTO for weather forecast information
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ForecastDTO {

    private String locationName;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String country;
    private List<DailyForecast> dailyForecasts;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DailyForecast {
        private LocalDate date;
        private BigDecimal tempMin;
        private BigDecimal tempMax;
        private BigDecimal tempAvg;
        private String weatherCondition;
        private String weatherDescription;
        private String icon;
        private Integer humidity;
        private BigDecimal windSpeed;
        private Integer pressure;
        private Integer clouds;
        private BigDecimal pop; // Probability of precipitation
    }
}

