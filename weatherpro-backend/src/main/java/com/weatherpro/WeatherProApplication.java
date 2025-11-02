package com.weatherpro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * WeatherPro Application - Tech Assessment 2
 * A weather application with CRUD operations, API integrations, and data export
 * 
 * @author Your Name
 */
@SpringBootApplication
public class WeatherProApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeatherProApplication.class, args);
        System.out.println("\n===========================================");
        System.out.println("🌤️  WeatherPro API Started Successfully!");
        System.out.println("===========================================");
        System.out.println("📍 API Base URL: http://localhost:8080/api");
        System.out.println("📚 Swagger UI: http://localhost:8080/api/swagger-ui.html");
        System.out.println("===========================================\n");
    }

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
}

