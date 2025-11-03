package com.weatherpro.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.weatherpro.exception.InvalidLocationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Service for validating and normalizing location input with fuzzy matching
 */
@Service
@Slf4j
public class LocationValidationService {

    private final WebClient geoWebClient;
    private final String apiKey;
    private final LevenshteinDistance levenshtein;
    private final GoogleMapsService googleMapsService;

    public LocationValidationService(
            @Qualifier("openWeatherGeoWebClient") WebClient geoWebClient,
            @Value("${openweather.api.key}") String apiKey,
            GoogleMapsService googleMapsService) {
        this.geoWebClient = geoWebClient;
        this.apiKey = apiKey;
        this.levenshtein = new LevenshteinDistance();
        this.googleMapsService = googleMapsService;
    }

    /**
     * Validate and normalize location input
     * Supports: ZIP codes, coordinates, city names, landmarks
     */
    public LocationInfo validateAndNormalizeLocation(String location) {
        log.info("Validating location: {}", location);

        // Try to parse as coordinates first
        LocationInfo coordInfo = parseCoordinates(location);
        if (coordInfo != null) {
            return enrichWithReverseGeocode(coordInfo);
        }

        // Try to parse as ZIP code
        if (isZipCode(location)) {
            return geocodeZipCode(location);
        }

        // Try landmark search using Google Geocoding API (works for landmarks and places)
        GoogleMapsService.LandmarkInfo landmark = googleMapsService.findLandmark(location);
        if (landmark.isFound()) {
            log.info("Found landmark via Google: {} at {},{}", 
                     landmark.getName(), landmark.getLatitude(), landmark.getLongitude());
            
            return LocationInfo.builder()
                    .locationName(landmark.getFormattedAddress())
                    .latitude(landmark.getLatitude())
                    .longitude(landmark.getLongitude())
                    .locationType("landmark")
                    .originalInput(location)
                    .validated(true)
                    .build();
        }

        // Finally, try city search using OpenWeatherMap as fallback
        return geocodeCityOrLandmark(location);
    }

    /**
     * Parse coordinates from various formats
     * Supports: 
     * - "40.7128,-74.0060"
     * - "40.7128, -74.0060"
     * - "40.7128° N, 74.0060° W"
     * - "22.7196° N, 75.8577° E"
     * - "40.7128 N, 74.0060 W"
     */
    private LocationInfo parseCoordinates(String input) {
        // Remove degree symbols and direction indicators for parsing
        String cleanInput = input.replaceAll("°", "").trim();
        
        // Pattern for decimal coordinates with optional spaces
        Pattern pattern = Pattern.compile(
            "(-?\\d+\\.\\d+)\\s*[°]?\\s*([NS])?[,\\s]+(-?\\d+\\.\\d+)\\s*[°]?\\s*([EW])?",
            Pattern.CASE_INSENSITIVE
        );
        Matcher matcher = pattern.matcher(cleanInput);

        if (matcher.find()) {
            try {
                BigDecimal lat = new BigDecimal(matcher.group(1));
                BigDecimal lon = new BigDecimal(matcher.group(3));
                
                // Handle direction indicators (S and W should be negative)
                String latDir = matcher.group(2);
                String lonDir = matcher.group(4);
                
                if (latDir != null && latDir.toUpperCase().equals("S") && lat.compareTo(BigDecimal.ZERO) > 0) {
                    lat = lat.negate();
                }
                if (lonDir != null && lonDir.toUpperCase().equals("W") && lon.compareTo(BigDecimal.ZERO) > 0) {
                    lon = lon.negate();
                }

                // Validate coordinate ranges
                if (lat.abs().compareTo(BigDecimal.valueOf(90)) <= 0 &&
                    lon.abs().compareTo(BigDecimal.valueOf(180)) <= 0) {

                    return LocationInfo.builder()
                            .latitude(lat)
                            .longitude(lon)
                            .locationType("coordinates")
                            .originalInput(input)
                            .build();
                }
            } catch (NumberFormatException e) {
                log.warn("Failed to parse coordinates: {}", input);
            }
        }
        return null;
    }

    /**
     * Check if input looks like a ZIP/postal code
     */
    private boolean isZipCode(String input) {
        // US ZIP: 12345 or 12345-6789
        // Indian PIN: 110001 (6 digits)
        // Canadian: K1A 0B1
        // UK: SW1A 1AA
        return input.matches("^\\d{5}(-\\d{4})?$") ||          // US ZIP
               input.matches("^\\d{6}$") ||                     // Indian PIN code
               input.matches("^[A-Z]\\d[A-Z] \\d[A-Z]\\d$") || // Canadian
               input.matches("^[A-Z]{1,2}\\d{1,2}[A-Z]? \\d[A-Z]{2}$"); // UK
    }

    /**
     * Geocode ZIP/postal code
     * For Indian PIN codes, append country code
     */
    private LocationInfo geocodeZipCode(String zipCode) {
        try {
            // For Indian PIN codes (6 digits), append country code
            // Must be final for use in lambda
            final String queryZip = zipCode.matches("^\\d{6}$") ? zipCode + ",IN" : zipCode;
            
            JsonNode response = geoWebClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/zip")
                            .queryParam("zip", queryZip)
                            .queryParam("appid", apiKey)
                            .build())
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();

            if (response != null && !response.has("cod")) {
                return LocationInfo.builder()
                        .locationName(response.get("name").asText() + ", " + response.get("country").asText())
                        .latitude(new BigDecimal(response.get("lat").asText()))
                        .longitude(new BigDecimal(response.get("lon").asText()))
                        .country(response.get("country").asText())
                        .locationType("zip")
                        .originalInput(zipCode)
                        .validated(true)
                        .build();
            }
        } catch (Exception e) {
            log.error("Failed to geocode ZIP/PIN code: {}", zipCode, e);
        }

        throw new InvalidLocationException("Invalid ZIP/postal/PIN code: " + zipCode);
    }

    /**
     * Geocode city name or landmark with fuzzy matching
     * OpenWeatherMap Geocoding API can handle both cities and major landmarks
     */
    private LocationInfo geocodeCityOrLandmark(String location) {
        try {
            JsonNode response = geoWebClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/direct")
                            .queryParam("q", location)
                            .queryParam("limit", 5)
                            .queryParam("appid", apiKey)
                            .build())
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();

            if (response != null && response.isArray() && response.size() > 0) {
                // Get the best match using fuzzy matching
                JsonNode bestMatch = findBestMatch(location, response);

                String locationName = bestMatch.get("name").asText();
                String country = bestMatch.get("country").asText();
                String state = bestMatch.has("state") ? bestMatch.get("state").asText() : null;

                String fullName = locationName + 
                                (state != null ? ", " + state : "") + 
                                ", " + country;

                return LocationInfo.builder()
                        .locationName(fullName)
                        .latitude(new BigDecimal(bestMatch.get("lat").asText()))
                        .longitude(new BigDecimal(bestMatch.get("lon").asText()))
                        .country(country)
                        .state(state)
                        .locationType("city")
                        .originalInput(location)
                        .validated(true)
                        .build();
            }
        } catch (Exception e) {
            log.error("Failed to geocode location: {}", location, e);
        }

        throw new InvalidLocationException(
            "Could not find location: " + location + 
            ". Please check spelling or try a different format."
        );
    }

    /**
     * Find best match using Levenshtein distance for fuzzy matching
     */
    private JsonNode findBestMatch(String input, JsonNode results) {
        JsonNode bestMatch = results.get(0);
        int bestDistance = Integer.MAX_VALUE;

        for (JsonNode result : results) {
            String name = result.get("name").asText().toLowerCase();
            int distance = levenshtein.apply(input.toLowerCase(), name);

            if (distance < bestDistance) {
                bestDistance = distance;
                bestMatch = result;
            }
        }

        log.info("Fuzzy match: '{}' -> '{}' (distance: {})", 
                 input, bestMatch.get("name").asText(), bestDistance);

        return bestMatch;
    }

    /**
     * Enrich coordinate-based location with reverse geocoding
     */
    private LocationInfo enrichWithReverseGeocode(LocationInfo coordInfo) {
        try {
            JsonNode response = geoWebClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/reverse")
                            .queryParam("lat", coordInfo.getLatitude())
                            .queryParam("lon", coordInfo.getLongitude())
                            .queryParam("limit", 1)
                            .queryParam("appid", apiKey)
                            .build())
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();

            if (response != null && response.isArray() && response.size() > 0) {
                JsonNode location = response.get(0);
                coordInfo.setLocationName(location.get("name").asText() + 
                                        ", " + location.get("country").asText());
                coordInfo.setCountry(location.get("country").asText());
                if (location.has("state")) {
                    coordInfo.setState(location.get("state").asText());
                }
                coordInfo.setValidated(true);
            }
        } catch (Exception e) {
            log.warn("Failed to reverse geocode coordinates", e);
            coordInfo.setLocationName("Unknown Location");
            coordInfo.setValidated(false);
        }

        return coordInfo;
    }

    /**
     * Location information structure
     */
    @lombok.Data
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class LocationInfo {
        private String locationName;
        private BigDecimal latitude;
        private BigDecimal longitude;
        private String country;
        private String state;
        private String locationType;
        private String originalInput;
        private Boolean validated;
    }
}

