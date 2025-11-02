package com.weatherpro.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.weatherpro.dto.CurrentWeatherDTO;
import com.weatherpro.dto.ForecastDTO;
import com.weatherpro.dto.WeatherRequestDTO;
import com.weatherpro.dto.WeatherResponseDTO;
import com.weatherpro.exception.DuplicateWeatherRecordException;
import com.weatherpro.exception.InvalidDateRangeException;
import com.weatherpro.exception.WeatherRecordNotFoundException;
import com.weatherpro.model.Location;
import com.weatherpro.model.WeatherRecord;
import com.weatherpro.repository.LocationRepository;
import com.weatherpro.repository.WeatherRecordRepository;
import com.weatherpro.service.LocationValidationService.LocationInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service for weather operations and OpenWeatherMap API integration
 */
@Service
@Slf4j
public class WeatherService {

    private final WeatherRecordRepository weatherRecordRepository;
    private final LocationRepository locationRepository;
    private final LocationValidationService locationValidationService;
    private final WebClient weatherWebClient;
    
    @Value("${openweather.api.key}")
    private String apiKey;
    
    // Constructor with @Qualifier for WebClient
    public WeatherService(
            WeatherRecordRepository weatherRecordRepository,
            LocationRepository locationRepository,
            LocationValidationService locationValidationService,
            @Qualifier("openWeatherWebClient") WebClient weatherWebClient) {
        this.weatherRecordRepository = weatherRecordRepository;
        this.locationRepository = locationRepository;
        this.locationValidationService = locationValidationService;
        this.weatherWebClient = weatherWebClient;
    }

    /**
     * CREATE: Create a new weather record
     */
    public WeatherResponseDTO createWeatherRecord(WeatherRequestDTO request) {
        log.info("Creating weather record for location: {}", request.getLocation());

        // Validate date range
        validateDateRange(request.getStartDate(), request.getEndDate());

        // Validate and normalize location
        LocationInfo locationInfo = locationValidationService.validateAndNormalizeLocation(
            request.getLocation()
        );
        
        // Check for duplicate records (same location and overlapping dates)
        boolean isDuplicate = weatherRecordRepository.existsDuplicateRecord(
            locationInfo.getLocationName(),
            request.getStartDate(),
            request.getEndDate()
        );
        
        if (isDuplicate) {
            String dateRange = request.getStartDate() + " to " + request.getEndDate();
            throw new DuplicateWeatherRecordException(locationInfo.getLocationName(), dateRange);
        }

        // Fetch current weather data
        CurrentWeatherDTO currentWeather = getCurrentWeather(
            locationInfo.getLatitude(),
            locationInfo.getLongitude()
        );

        // Save or update location in locations table
        saveOrUpdateLocation(locationInfo);

        // Create and save weather record
        WeatherRecord record = WeatherRecord.builder()
                .locationName(locationInfo.getLocationName())
                .locationType(locationInfo.getLocationType())
                .latitude(locationInfo.getLatitude())
                .longitude(locationInfo.getLongitude())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .temperature(currentWeather.getTemperature())
                .feelsLike(currentWeather.getFeelsLike())
                .humidity(currentWeather.getHumidity())
                .weatherCondition(currentWeather.getWeatherCondition())
                .weatherDescription(currentWeather.getWeatherDescription())
                .windSpeed(currentWeather.getWindSpeed())
                .pressure(currentWeather.getPressure())
                .icon(currentWeather.getIcon())
                .aqi(currentWeather.getAqi())
                .aqiCategory(currentWeather.getAqiCategory())
                .country(locationInfo.getCountry())
                .state(locationInfo.getState())
                .createdBy(request.getCreatedBy())
                .build();

        WeatherRecord savedRecord = weatherRecordRepository.save(record);
        log.info("Weather record created with ID: {}", savedRecord.getId());

        return mapToResponseDTO(savedRecord);
    }

    /**
     * READ: Get all weather records
     */
    public List<WeatherResponseDTO> getAllWeatherRecords() {
        log.info("Fetching all weather records");
        return weatherRecordRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * READ: Get weather record by ID
     */
    public WeatherResponseDTO getWeatherRecordById(UUID id) {
        log.info("Fetching weather record with ID: {}", id);
        WeatherRecord record = weatherRecordRepository.findById(id)
                .orElseThrow(() -> new WeatherRecordNotFoundException(id));
        return mapToResponseDTO(record);
    }

    /**
     * READ: Search weather records by location
     */
    public List<WeatherResponseDTO> searchByLocation(String location) {
        log.info("Searching weather records for location: {}", location);
        return weatherRecordRepository.findByLocationNameContainingIgnoreCase(location)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * UPDATE: Update an existing weather record
     */
    public WeatherResponseDTO updateWeatherRecord(UUID id, WeatherRequestDTO request) {
        log.info("Updating weather record with ID: {}", id);

        WeatherRecord record = weatherRecordRepository.findById(id)
                .orElseThrow(() -> new WeatherRecordNotFoundException(id));

        // Validate date range
        validateDateRange(request.getStartDate(), request.getEndDate());

        // Validate new location if provided
        if (request.getLocation() != null && !request.getLocation().isEmpty()) {
            LocationInfo locationInfo = locationValidationService.validateAndNormalizeLocation(
                request.getLocation()
            );

            // Fetch updated weather data
            CurrentWeatherDTO currentWeather = getCurrentWeather(
                locationInfo.getLatitude(),
                locationInfo.getLongitude()
            );

            // Update location and weather data
            record.setLocationName(locationInfo.getLocationName());
            record.setLocationType(locationInfo.getLocationType());
            record.setLatitude(locationInfo.getLatitude());
            record.setLongitude(locationInfo.getLongitude());
            record.setCountry(locationInfo.getCountry());
            record.setState(locationInfo.getState());
            record.setTemperature(currentWeather.getTemperature());
            record.setFeelsLike(currentWeather.getFeelsLike());
            record.setHumidity(currentWeather.getHumidity());
            record.setWeatherCondition(currentWeather.getWeatherCondition());
            record.setWeatherDescription(currentWeather.getWeatherDescription());
            record.setWindSpeed(currentWeather.getWindSpeed());
            record.setPressure(currentWeather.getPressure());
            record.setIcon(currentWeather.getIcon());
            record.setAqi(currentWeather.getAqi());
            record.setAqiCategory(currentWeather.getAqiCategory());
        }

        // Update date range
        record.setStartDate(request.getStartDate());
        record.setEndDate(request.getEndDate());

        WeatherRecord updatedRecord = weatherRecordRepository.save(record);
        log.info("Weather record updated: {}", id);

        return mapToResponseDTO(updatedRecord);
    }

    /**
     * DELETE: Delete a weather record
     */
    public void deleteWeatherRecord(UUID id) {
        log.info("Deleting weather record with ID: {}", id);
        
        if (!weatherRecordRepository.existsById(id)) {
            throw new WeatherRecordNotFoundException(id);
        }
        
        weatherRecordRepository.deleteById(id);
        log.info("Weather record deleted: {}", id);
    }

    /**
     * Get current weather for a location
     */
    public CurrentWeatherDTO getCurrentWeather(BigDecimal lat, BigDecimal lon) {
        log.info("Fetching current weather for coordinates: {}, {}", lat, lon);

        try {
            // Fetch weather data
            JsonNode response = weatherWebClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/weather")
                            .queryParam("lat", lat)
                            .queryParam("lon", lon)
                            .queryParam("units", "metric")
                            .queryParam("appid", apiKey)
                            .build())
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();

            // Fetch AQI data
            Map<String, Object> aqiData = getAirQuality(lat, lon);

            if (response != null) {
                return CurrentWeatherDTO.builder()
                        .locationName(response.get("name").asText())
                        .latitude(lat)
                        .longitude(lon)
                        .temperature(new BigDecimal(response.get("main").get("temp").asText()))
                        .feelsLike(new BigDecimal(response.get("main").get("feels_like").asText()))
                        .humidity(response.get("main").get("humidity").asInt())
                        .weatherCondition(response.get("weather").get(0).get("main").asText())
                        .weatherDescription(response.get("weather").get(0).get("description").asText())
                        .windSpeed(new BigDecimal(response.get("wind").get("speed").asText()))
                        .pressure(response.get("main").get("pressure").asInt())
                        .icon(response.get("weather").get(0).get("icon").asText())
                        .aqi((Integer) aqiData.get("aqi"))
                        .aqiCategory((String) aqiData.get("category"))
                        .sunrise(response.get("sys").get("sunrise").asLong())
                        .sunset(response.get("sys").get("sunset").asLong())
                        .visibility(response.get("visibility").asInt())
                        .clouds(response.get("clouds").get("all").asInt())
                        .country(response.get("sys").get("country").asText())
                        .build();
            }
        } catch (Exception e) {
            log.error("Failed to fetch current weather", e);
            throw new RuntimeException("Failed to fetch weather data: " + e.getMessage());
        }

        throw new RuntimeException("Failed to fetch weather data");
    }
    
    /**
     * Get Air Quality Index from OpenWeatherMap API
     */
    private Map<String, Object> getAirQuality(BigDecimal lat, BigDecimal lon) {
        try {
            JsonNode response = weatherWebClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/air_pollution")
                            .queryParam("lat", lat)
                            .queryParam("lon", lon)
                            .queryParam("appid", apiKey)
                            .build())
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();

            if (response != null && response.has("list") && response.get("list").size() > 0) {
                int aqi = response.get("list").get(0).get("main").get("aqi").asInt();
                String category = getAqiCategory(aqi);
                
                return Map.of("aqi", aqi, "category", category);
            }
        } catch (Exception e) {
            log.warn("Failed to fetch AQI data: {}", e.getMessage());
        }
        
        // Return default if API fails
        return Map.of("aqi", 0, "category", "Unknown");
    }
    
    /**
     * Convert AQI number to category
     * 1 = Good, 2 = Fair, 3 = Moderate, 4 = Poor, 5 = Very Poor
     */
    private String getAqiCategory(int aqi) {
        return switch (aqi) {
            case 1 -> "Good";
            case 2 -> "Fair";
            case 3 -> "Moderate";
            case 4 -> "Poor";
            case 5 -> "Very Poor";
            default -> "Unknown";
        };
    }

    /**
     * Get 5-day forecast
     */
    public ForecastDTO getFiveDayForecast(BigDecimal lat, BigDecimal lon) {
        log.info("Fetching 5-day forecast for coordinates: {}, {}", lat, lon);

        try {
            JsonNode response = weatherWebClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/forecast")
                            .queryParam("lat", lat)
                            .queryParam("lon", lon)
                            .queryParam("units", "metric")
                            .queryParam("appid", apiKey)
                            .build())
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();

            if (response != null) {
                JsonNode city = response.get("city");
                JsonNode list = response.get("list");

                // Group forecasts by day
                Map<LocalDate, List<JsonNode>> forecastsByDay = new HashMap<>();
                list.forEach(item -> {
                    long timestamp = item.get("dt").asLong();
                    LocalDate date = Instant.ofEpochSecond(timestamp)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate();
                    forecastsByDay.computeIfAbsent(date, k -> new ArrayList<>()).add(item);
                });

                // Calculate daily statistics
                List<ForecastDTO.DailyForecast> dailyForecasts = forecastsByDay.entrySet().stream()
                        .sorted(Map.Entry.comparingByKey())
                        .limit(5)
                        .map(entry -> calculateDailyForecast(entry.getKey(), entry.getValue()))
                        .collect(Collectors.toList());

                return ForecastDTO.builder()
                        .locationName(city.get("name").asText())
                        .latitude(new BigDecimal(city.get("coord").get("lat").asText()))
                        .longitude(new BigDecimal(city.get("coord").get("lon").asText()))
                        .country(city.get("country").asText())
                        .dailyForecasts(dailyForecasts)
                        .build();
            }
        } catch (Exception e) {
            log.error("Failed to fetch forecast", e);
            throw new RuntimeException("Failed to fetch forecast data: " + e.getMessage());
        }

        throw new RuntimeException("Failed to fetch forecast data");
    }

    /**
     * Calculate daily forecast from multiple time points
     */
    private ForecastDTO.DailyForecast calculateDailyForecast(LocalDate date, List<JsonNode> forecasts) {
        DoubleSummaryStatistics tempStats = forecasts.stream()
                .mapToDouble(f -> f.get("main").get("temp").asDouble())
                .summaryStatistics();

        JsonNode representative = forecasts.get(forecasts.size() / 2); // Use midday forecast

        return ForecastDTO.DailyForecast.builder()
                .date(date)
                .tempMin(BigDecimal.valueOf(tempStats.getMin()))
                .tempMax(BigDecimal.valueOf(tempStats.getMax()))
                .tempAvg(BigDecimal.valueOf(tempStats.getAverage()))
                .weatherCondition(representative.get("weather").get(0).get("main").asText())
                .weatherDescription(representative.get("weather").get(0).get("description").asText())
                .icon(representative.get("weather").get(0).get("icon").asText())
                .humidity(representative.get("main").get("humidity").asInt())
                .windSpeed(new BigDecimal(representative.get("wind").get("speed").asText()))
                .pressure(representative.get("main").get("pressure").asInt())
                .clouds(representative.get("clouds").get("all").asInt())
                .pop(representative.has("pop") ? 
                     new BigDecimal(representative.get("pop").asText()) : BigDecimal.ZERO)
                .build();
    }

    /**
     * Validate date range
     */
    private void validateDateRange(LocalDate startDate, LocalDate endDate) {
        LocalDate today = LocalDate.now();
        LocalDate maxFutureDate = today.plusDays(16); // OpenWeatherMap limit
        LocalDate maxPastDate = today.minusYears(1);

        if (startDate.isAfter(endDate)) {
            throw new InvalidDateRangeException("Start date must be before or equal to end date");
        }

        if (startDate.isBefore(maxPastDate)) {
            throw new InvalidDateRangeException("Start date cannot be more than 1 year in the past");
        }

        if (endDate.isAfter(maxFutureDate)) {
            throw new InvalidDateRangeException("End date cannot be more than 16 days in the future");
        }
    }

    /**
     * Save or update location in locations table
     * This table tracks all validated locations separately for reference
     */
    private void saveOrUpdateLocation(LocationInfo locationInfo) {
        try {
            // Check if location already exists by name
            Optional<Location> existing = locationRepository.findByLocationNameIgnoreCase(
                    locationInfo.getLocationName());
            
            if (existing.isEmpty()) {
                // Create new location entry
                Location location = Location.builder()
                        .locationName(locationInfo.getLocationName())
                        .locationType(locationInfo.getLocationType())
                        .latitude(locationInfo.getLatitude())
                        .longitude(locationInfo.getLongitude())
                        .country(locationInfo.getCountry())
                        .state(locationInfo.getState())
                        .validated(locationInfo.getValidated())
                        .build();
                
                locationRepository.save(location);
                log.info("New location saved to locations table: {}", locationInfo.getLocationName());
            } else {
                log.debug("Location already exists in locations table: {}", locationInfo.getLocationName());
            }
        } catch (Exception e) {
            // Don't fail the main operation if location save fails
            log.warn("Failed to save location to locations table: {}", e.getMessage());
        }
    }

    /**
     * Map entity to response DTO
     */
    private WeatherResponseDTO mapToResponseDTO(WeatherRecord record) {
        return WeatherResponseDTO.builder()
                .id(record.getId())
                .locationName(record.getLocationName())
                .locationType(record.getLocationType())
                .latitude(record.getLatitude())
                .longitude(record.getLongitude())
                .startDate(record.getStartDate())
                .endDate(record.getEndDate())
                .temperature(record.getTemperature())
                .feelsLike(record.getFeelsLike())
                .humidity(record.getHumidity())
                .weatherCondition(record.getWeatherCondition())
                .weatherDescription(record.getWeatherDescription())
                .windSpeed(record.getWindSpeed())
                .pressure(record.getPressure())
                .icon(record.getIcon())
                .aqi(record.getAqi())
                .aqiCategory(record.getAqiCategory())
                .country(record.getCountry())
                .state(record.getState())
                .additionalInfo(record.getAdditionalInfo())
                .createdAt(record.getCreatedAt())
                .updatedAt(record.getUpdatedAt())
                .createdBy(record.getCreatedBy())
                .build();
    }
}

