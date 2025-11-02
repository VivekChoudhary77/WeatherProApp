package com.weatherpro.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entity representing a weather record stored in the database
 */
@Entity
@Table(name = "weather_records")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeatherRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String locationName;

    @Column(nullable = false)
    private String locationType; // zip, coordinates, city, landmark

    @Column(precision = 10, scale = 6)
    private BigDecimal latitude;

    @Column(precision = 10, scale = 6)
    private BigDecimal longitude;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(precision = 5, scale = 2)
    private BigDecimal temperature; // in Celsius

    @Column(precision = 5, scale = 2)
    private BigDecimal feelsLike;

    private Integer humidity; // percentage

    private String weatherCondition; // Clear, Clouds, Rain, etc.

    private String weatherDescription; // clear sky, few clouds, etc.

    @Column(precision = 5, scale = 2)
    private BigDecimal windSpeed; // m/s

    private Integer pressure; // hPa

    private String icon; // weather icon code
    
    private Integer aqi; // Air Quality Index (1-5: 1=Good, 5=Very Poor)
    
    @Column(length = 50)
    private String aqiCategory; // Good, Fair, Moderate, Poor, Very Poor

    private String country;

    private String state;

    @Column(length = 1000)
    private String additionalInfo;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private String createdBy; // username/user identifier (optional for future)
}

