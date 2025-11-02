package com.weatherpro.service;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

/**
 * Service for YouTube API integration
 */
@Service
@Slf4j
public class YoutubeService {

    private final WebClient youtubeWebClient;

    @Value("${youtube.api.key}")
    private String apiKey;
    
    // Constructor with @Qualifier for WebClient
    public YoutubeService(@Qualifier("youtubeWebClient") WebClient youtubeWebClient) {
        this.youtubeWebClient = youtubeWebClient;
    }

    /**
     * Search for videos about a location
     */
    public List<VideoInfo> searchLocationVideos(String location) {
        log.info("Searching YouTube videos for location: {}", location);

        try {
            String searchQuery = location + " travel guide tour";

            JsonNode response = youtubeWebClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/search")
                            .queryParam("part", "snippet")
                            .queryParam("q", searchQuery)
                            .queryParam("type", "video")
                            .queryParam("maxResults", 5)
                            .queryParam("key", apiKey)
                            .build())
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();

            List<VideoInfo> videos = new ArrayList<>();
            if (response != null && response.has("items")) {
                JsonNode items = response.get("items");
                items.forEach(item -> {
                    JsonNode snippet = item.get("snippet");
                    JsonNode id = item.get("id");
                    
                    VideoInfo video = VideoInfo.builder()
                            .videoId(id.get("videoId").asText())
                            .title(snippet.get("title").asText())
                            .description(snippet.get("description").asText())
                            .channelTitle(snippet.get("channelTitle").asText())
                            .publishedAt(snippet.get("publishedAt").asText())
                            .thumbnailUrl(snippet.get("thumbnails").get("high").get("url").asText())
                            .build();
                    
                    videos.add(video);
                });
            }

            log.info("Found {} videos for location: {}", videos.size(), location);
            return videos;

        } catch (Exception e) {
            log.error("Failed to fetch YouTube videos for location: {}", location, e);
            return new ArrayList<>();
        }
    }

    @lombok.Data
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class VideoInfo {
        private String videoId;
        private String title;
        private String description;
        private String channelTitle;
        private String publishedAt;
        private String thumbnailUrl;
    }
}

