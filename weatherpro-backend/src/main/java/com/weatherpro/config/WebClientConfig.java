package com.weatherpro.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Configuration for WebClient used in API calls
 */
@Configuration
public class WebClientConfig {

    @Bean(name = "openWeatherWebClient")
    public WebClient openWeatherWebClient(WebClient.Builder builder) {
        return builder
                .baseUrl("https://api.openweathermap.org/data/2.5")
                .build();
    }

    @Bean(name = "openWeatherGeoWebClient")
    public WebClient openWeatherGeoWebClient(WebClient.Builder builder) {
        return builder
                .baseUrl("https://api.openweathermap.org/geo/1.0")
                .build();
    }

    @Bean(name = "youtubeWebClient")
    public WebClient youtubeWebClient(WebClient.Builder builder) {
        return builder
                .baseUrl("https://www.googleapis.com/youtube/v3")
                .build();
    }

    @Bean(name = "googleMapsWebClient")
    public WebClient googleMapsWebClient(WebClient.Builder builder) {
        return builder
                .baseUrl("https://maps.googleapis.com/maps/api")
                .build();
    }
}

