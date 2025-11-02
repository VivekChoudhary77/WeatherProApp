package com.weatherpro.controller;

import com.weatherpro.service.GoogleMapsService;
import com.weatherpro.service.YoutubeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * REST Controller for third-party API integrations
 */
@RestController
@RequestMapping("/integration")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class IntegrationController {

    private final YoutubeService youtubeService;
    private final GoogleMapsService googleMapsService;

    /**
     * Get YouTube videos for a location
     */
    @GetMapping("/youtube")
    public ResponseEntity<List<YoutubeService.VideoInfo>> getYoutubeVideos(
            @RequestParam String location) {
        log.info("GET /integration/youtube?location={}", location);
        List<YoutubeService.VideoInfo> videos = youtubeService.searchLocationVideos(location);
        return ResponseEntity.ok(videos);
    }

    /**
     * Get map information for a location
     */
    @GetMapping("/maps")
    public ResponseEntity<GoogleMapsService.MapInfo> getMapInfo(
            @RequestParam BigDecimal lat,
            @RequestParam BigDecimal lon,
            @RequestParam String location) {
        log.info("GET /integration/maps?lat={}&lon={}&location={}", lat, lon, location);
        GoogleMapsService.MapInfo mapInfo = googleMapsService.getMapInfo(lat, lon, location);
        return ResponseEntity.ok(mapInfo);
    }

    /**
     * Search nearby places
     */
    @GetMapping("/maps/nearby")
    public ResponseEntity<GoogleMapsService.PlacesInfo> searchNearbyPlaces(
            @RequestParam BigDecimal lat,
            @RequestParam BigDecimal lon,
            @RequestParam(defaultValue = "tourist_attraction") String type) {
        log.info("GET /integration/maps/nearby?lat={}&lon={}&type={}", lat, lon, type);
        GoogleMapsService.PlacesInfo places = googleMapsService.searchNearbyPlaces(lat, lon, type);
        return ResponseEntity.ok(places);
    }
}

