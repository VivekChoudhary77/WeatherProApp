package com.weatherpro.controller;

import com.weatherpro.dto.CurrentWeatherDTO;
import com.weatherpro.dto.ForecastDTO;
import com.weatherpro.dto.WeatherRequestDTO;
import com.weatherpro.dto.WeatherResponseDTO;
import com.weatherpro.service.WeatherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * REST Controller for weather operations
 */
@RestController
@RequestMapping("/weather")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class WeatherController {

    private final WeatherService weatherService;

    /**
     * CREATE: Create a new weather record
     */
    @PostMapping
    public ResponseEntity<WeatherResponseDTO> createWeatherRecord(
            @Valid @RequestBody WeatherRequestDTO request) {
        log.info("POST /weather - Creating weather record");
        WeatherResponseDTO response = weatherService.createWeatherRecord(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * READ: Get all weather records
     */
    @GetMapping
    public ResponseEntity<List<WeatherResponseDTO>> getAllWeatherRecords() {
        log.info("GET /weather - Fetching all records");
        List<WeatherResponseDTO> records = weatherService.getAllWeatherRecords();
        return ResponseEntity.ok(records);
    }

    /**
     * READ: Get weather record by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<WeatherResponseDTO> getWeatherRecordById(@PathVariable UUID id) {
        log.info("GET /weather/{} - Fetching record by ID", id);
        WeatherResponseDTO record = weatherService.getWeatherRecordById(id);
        return ResponseEntity.ok(record);
    }

    /**
     * READ: Search weather records by location
     */
    @GetMapping("/search")
    public ResponseEntity<List<WeatherResponseDTO>> searchByLocation(
            @RequestParam String location) {
        log.info("GET /weather/search?location={}", location);
        List<WeatherResponseDTO> records = weatherService.searchByLocation(location);
        return ResponseEntity.ok(records);
    }

    /**
     * UPDATE: Update a weather record
     */
    @PutMapping("/{id}")
    public ResponseEntity<WeatherResponseDTO> updateWeatherRecord(
            @PathVariable UUID id,
            @Valid @RequestBody WeatherRequestDTO request) {
        log.info("PUT /weather/{} - Updating record", id);
        WeatherResponseDTO updated = weatherService.updateWeatherRecord(id, request);
        return ResponseEntity.ok(updated);
    }

    /**
     * DELETE: Delete a weather record
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWeatherRecord(@PathVariable UUID id) {
        log.info("DELETE /weather/{} - Deleting record", id);
        weatherService.deleteWeatherRecord(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Get current weather for coordinates
     */
    @GetMapping("/current")
    public ResponseEntity<CurrentWeatherDTO> getCurrentWeather(
            @RequestParam BigDecimal lat,
            @RequestParam BigDecimal lon) {
        log.info("GET /weather/current?lat={}&lon={}", lat, lon);
        CurrentWeatherDTO weather = weatherService.getCurrentWeather(lat, lon);
        return ResponseEntity.ok(weather);
    }

    /**
     * Get 5-day forecast for coordinates
     */
    @GetMapping("/forecast")
    public ResponseEntity<ForecastDTO> getForecast(
            @RequestParam BigDecimal lat,
            @RequestParam BigDecimal lon) {
        log.info("GET /weather/forecast?lat={}&lon={}", lat, lon);
        ForecastDTO forecast = weatherService.getFiveDayForecast(lat, lon);
        return ResponseEntity.ok(forecast);
    }
}

