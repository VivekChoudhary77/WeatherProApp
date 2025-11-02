package com.weatherpro.service;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;

/**
 * Service for Google Maps API integration
 */
@Service
@Slf4j
public class GoogleMapsService {

    private final WebClient mapsWebClient;

    @Value("${google.maps.api.key}")
    private String apiKey;
    
    // Constructor with @Qualifier for WebClient
    public GoogleMapsService(@Qualifier("googleMapsWebClient") WebClient mapsWebClient) {
        this.mapsWebClient = mapsWebClient;
    }

    /**
     * Get map data for a location
     */
    public MapInfo getMapInfo(BigDecimal latitude, BigDecimal longitude, String locationName) {
        log.info("Getting map info for: {}", locationName);

        // Construct map URLs
        String staticMapUrl = String.format(
            "https://maps.googleapis.com/maps/api/staticmap?center=%s,%s&zoom=12&size=600x400&markers=color:red%%7C%s,%s&key=%s",
            latitude, longitude, latitude, longitude, apiKey
        );

        String embedUrl = String.format(
            "https://www.google.com/maps/embed/v1/place?key=%s&q=%s,%s",
            apiKey, latitude, longitude
        );

        return MapInfo.builder()
                .latitude(latitude)
                .longitude(longitude)
                .locationName(locationName)
                .staticMapUrl(staticMapUrl)
                .embedUrl(embedUrl)
                .googleMapsUrl(String.format("https://www.google.com/maps/search/?api=1&query=%s,%s", latitude, longitude))
                .build();
    }

    /**
     * Search for a landmark or place by name using Google Geocoding API
     * This is useful for finding specific landmarks like "Eiffel Tower", "Taj Mahal", etc.
     * Uses Geocoding API which is more commonly enabled than Places API
     */
    public LandmarkInfo findLandmark(String landmarkName) {
        log.info("Searching for landmark using Geocoding API: {}", landmarkName);

        try {
            JsonNode response = mapsWebClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/geocode/json")
                            .queryParam("address", landmarkName)
                            .queryParam("key", apiKey)
                            .build())
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();

            if (response != null && "OK".equals(response.get("status").asText())) {
                JsonNode results = response.get("results");
                if (results != null && results.isArray() && results.size() > 0) {
                    JsonNode firstResult = results.get(0);
                    JsonNode location = firstResult.get("geometry").get("location");
                    
                    // Extract name from formatted_address or use first address component
                    String formattedAddress = firstResult.get("formatted_address").asText();
                    String name = landmarkName; // Keep original input as name
                    
                    // Try to get a better name from address components if it's a landmark
                    JsonNode addressComponents = firstResult.get("address_components");
                    if (addressComponents != null && addressComponents.isArray() && addressComponents.size() > 0) {
                        JsonNode firstComponent = addressComponents.get(0);
                        if (firstComponent.has("long_name")) {
                            String longName = firstComponent.get("long_name").asText();
                            // Use the long_name if it seems like a landmark (not a number)
                            if (!longName.matches("^\\d+.*")) {
                                name = longName;
                            }
                        }
                    }

                    return LandmarkInfo.builder()
                            .found(true)
                            .name(name)
                            .formattedAddress(formattedAddress)
                            .latitude(new BigDecimal(location.get("lat").asText()))
                            .longitude(new BigDecimal(location.get("lng").asText()))
                            .build();
                }
            } else if (response != null) {
                log.warn("Geocoding API returned status: {}", response.get("status").asText());
            }
        } catch (Exception e) {
            log.error("Failed to find landmark: {}", landmarkName, e);
        }

        return LandmarkInfo.builder()
                .found(false)
                .build();
    }

    /**
     * Search for places near a location
     */
    public PlacesInfo searchNearbyPlaces(BigDecimal latitude, BigDecimal longitude, String type) {
        log.info("Searching nearby places of type: {} at {},{}", type, latitude, longitude);

        try {
            JsonNode response = mapsWebClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/place/nearbysearch/json")
                            .queryParam("location", latitude + "," + longitude)
                            .queryParam("radius", 5000) // 5km radius
                            .queryParam("type", type)
                            .queryParam("key", apiKey)
                            .build())
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();

            if (response != null && "OK".equals(response.get("status").asText())) {
                // Process results (simplified for demo)
                return PlacesInfo.builder()
                        .status("success")
                        .resultsCount(response.get("results").size())
                        .build();
            }
        } catch (Exception e) {
            log.error("Failed to search nearby places", e);
        }

        return PlacesInfo.builder()
                .status("error")
                .resultsCount(0)
                .build();
    }

    @lombok.Data
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class MapInfo {
        private BigDecimal latitude;
        private BigDecimal longitude;
        private String locationName;
        private String staticMapUrl;
        private String embedUrl;
        private String googleMapsUrl;
    }

    @lombok.Data
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class PlacesInfo {
        private String status;
        private int resultsCount;
    }

    @lombok.Data
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class LandmarkInfo {
        private boolean found;
        private String name;
        private String formattedAddress;
        private BigDecimal latitude;
        private BigDecimal longitude;
    }
}

