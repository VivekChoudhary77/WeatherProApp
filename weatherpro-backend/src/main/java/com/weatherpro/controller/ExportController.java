package com.weatherpro.controller;

import com.weatherpro.dto.WeatherResponseDTO;
import com.weatherpro.service.ExportService;
import com.weatherpro.service.WeatherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for data export operations
 */
@RestController
@RequestMapping("/export")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class ExportController {

    private final WeatherService weatherService;
    private final ExportService exportService;

    /**
     * Export all weather records to JSON
     */
    @GetMapping("/json")
    public ResponseEntity<byte[]> exportToJson() {
        log.info("GET /export/json - Exporting to JSON");
        try {
            List<WeatherResponseDTO> records = weatherService.getAllWeatherRecords();
            byte[] data = exportService.exportToJson(records);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setContentDispositionFormData("attachment", "weather-data.json");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(data);
        } catch (Exception e) {
            log.error("Failed to export to JSON", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Export all weather records to CSV
     */
    @GetMapping("/csv")
    public ResponseEntity<byte[]> exportToCsv() {
        log.info("GET /export/csv - Exporting to CSV");
        try {
            List<WeatherResponseDTO> records = weatherService.getAllWeatherRecords();
            byte[] data = exportService.exportToCsv(records);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("text/csv"));
            headers.setContentDispositionFormData("attachment", "weather-data.csv");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(data);
        } catch (Exception e) {
            log.error("Failed to export to CSV", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Export all weather records to XML
     */
    @GetMapping("/xml")
    public ResponseEntity<byte[]> exportToXml() {
        log.info("GET /export/xml - Exporting to XML");
        try {
            List<WeatherResponseDTO> records = weatherService.getAllWeatherRecords();
            byte[] data = exportService.exportToXml(records);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_XML);
            headers.setContentDispositionFormData("attachment", "weather-data.xml");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(data);
        } catch (Exception e) {
            log.error("Failed to export to XML", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Export all weather records to Markdown
     */
    @GetMapping("/markdown")
    public ResponseEntity<byte[]> exportToMarkdown() {
        log.info("GET /export/markdown - Exporting to Markdown");
        try {
            List<WeatherResponseDTO> records = weatherService.getAllWeatherRecords();
            byte[] data = exportService.exportToMarkdown(records);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_PLAIN);
            headers.setContentDispositionFormData("attachment", "weather-data.md");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(data);
        } catch (Exception e) {
            log.error("Failed to export to Markdown", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Export all weather records to PDF
     */
    @GetMapping("/pdf")
    public ResponseEntity<byte[]> exportToPdf() {
        log.info("GET /export/pdf - Exporting to PDF");
        try {
            List<WeatherResponseDTO> records = weatherService.getAllWeatherRecords();
            byte[] data = exportService.exportToPdf(records);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "weather-data.pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(data);
        } catch (Exception e) {
            log.error("Failed to export to PDF", e);
            return ResponseEntity.internalServerError().build();
        }
    }
}

