package com.weatherpro.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.properties.UnitValue;
import com.opencsv.CSVWriter;
import com.weatherpro.dto.WeatherResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Service for exporting weather data in various formats
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ExportService {

    private final ObjectMapper objectMapper;

    /**
     * Export to JSON
     */
    public byte[] exportToJson(List<WeatherResponseDTO> records) throws Exception {
        log.info("Exporting {} records to JSON", records.size());
        
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        
        return mapper.writeValueAsBytes(records);
    }

    /**
     * Export to CSV
     */
    public byte[] exportToCsv(List<WeatherResponseDTO> records) throws Exception {
        log.info("Exporting {} records to CSV", records.size());
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (CSVWriter writer = new CSVWriter(new OutputStreamWriter(baos, StandardCharsets.UTF_8))) {
            
            // Write header
            String[] header = {
                "ID", "Location", "Type", "Latitude", "Longitude", 
                "Start Date", "End Date", "Temperature (°C)", "Feels Like (°C)",
                "Humidity (%)", "Condition", "Description", "Wind Speed (m/s)",
                "Pressure (hPa)", "Country", "State", "Created At"
            };
            writer.writeNext(header);
            
            // Write data
            for (WeatherResponseDTO record : records) {
                String[] row = {
                    record.getId() != null ? record.getId().toString() : "",
                    record.getLocationName() != null ? record.getLocationName() : "",
                    record.getLocationType() != null ? record.getLocationType() : "",
                    record.getLatitude() != null ? record.getLatitude().toString() : "",
                    record.getLongitude() != null ? record.getLongitude().toString() : "",
                    record.getStartDate() != null ? record.getStartDate().toString() : "",
                    record.getEndDate() != null ? record.getEndDate().toString() : "",
                    record.getTemperature() != null ? record.getTemperature().toString() : "",
                    record.getFeelsLike() != null ? record.getFeelsLike().toString() : "",
                    record.getHumidity() != null ? record.getHumidity().toString() : "",
                    record.getWeatherCondition() != null ? record.getWeatherCondition() : "",
                    record.getWeatherDescription() != null ? record.getWeatherDescription() : "",
                    record.getWindSpeed() != null ? record.getWindSpeed().toString() : "",
                    record.getPressure() != null ? record.getPressure().toString() : "",
                    record.getCountry() != null ? record.getCountry() : "",
                    record.getState() != null ? record.getState() : "",
                    record.getCreatedAt() != null ? record.getCreatedAt().toString() : ""
                };
                writer.writeNext(row);
            }
        }
        
        return baos.toByteArray();
    }

    /**
     * Export to XML
     */
    public byte[] exportToXml(List<WeatherResponseDTO> records) throws Exception {
        log.info("Exporting {} records to XML", records.size());
        
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.registerModule(new JavaTimeModule());
        xmlMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
        
        // Wrap in a root element
        WeatherRecordsWrapper wrapper = new WeatherRecordsWrapper(records);
        
        return xmlMapper.writeValueAsBytes(wrapper);
    }

    /**
     * Export to Markdown
     */
    public byte[] exportToMarkdown(List<WeatherResponseDTO> records) throws Exception {
        log.info("Exporting {} records to Markdown", records.size());
        
        StringBuilder md = new StringBuilder();
        
        md.append("# Weather Records Export\n\n");
        md.append("Generated: ").append(java.time.LocalDateTime.now()).append("\n\n");
        md.append("Total Records: ").append(records.size()).append("\n\n");
        md.append("---\n\n");
        
        for (WeatherResponseDTO record : records) {
            md.append("## ").append(record.getLocationName()).append("\n\n");
            md.append("- **ID**: ").append(record.getId()).append("\n");
            md.append("- **Location Type**: ").append(record.getLocationType()).append("\n");
            md.append("- **Coordinates**: ").append(record.getLatitude())
              .append(", ").append(record.getLongitude()).append("\n");
            md.append("- **Date Range**: ").append(record.getStartDate())
              .append(" to ").append(record.getEndDate()).append("\n");
            md.append("- **Temperature**: ").append(record.getTemperature()).append("°C\n");
            md.append("- **Feels Like**: ").append(record.getFeelsLike()).append("°C\n");
            md.append("- **Condition**: ").append(record.getWeatherCondition())
              .append(" - ").append(record.getWeatherDescription()).append("\n");
            md.append("- **Humidity**: ").append(record.getHumidity()).append("%\n");
            md.append("- **Wind Speed**: ").append(record.getWindSpeed()).append(" m/s\n");
            md.append("- **Pressure**: ").append(record.getPressure()).append(" hPa\n");
            
            if (record.getCountry() != null) {
                md.append("- **Country**: ").append(record.getCountry());
                if (record.getState() != null) {
                    md.append(", ").append(record.getState());
                }
                md.append("\n");
            }
            
            md.append("- **Created**: ").append(record.getCreatedAt()).append("\n");
            md.append("\n---\n\n");
        }
        
        return md.toString().getBytes(StandardCharsets.UTF_8);
    }

    /**
     * Export to PDF using iText7
     */
    public byte[] exportToPdf(List<WeatherResponseDTO> records) throws Exception {
        log.info("Exporting {} records to PDF", records.size());
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);
        
        // Add title
        Paragraph title = new Paragraph("Weather Records Export")
                .setFontSize(20)
                .setBold();
        document.add(title);
        
        document.add(new Paragraph("Generated: " + java.time.LocalDateTime.now()));
        document.add(new Paragraph("Total Records: " + records.size()));
        document.add(new Paragraph("\n"));
        
        // Create table
        float[] columnWidths = {2, 1.5f, 1, 1, 1.5f, 1.5f, 1};
        Table table = new Table(UnitValue.createPercentArray(columnWidths));
        table.setWidth(UnitValue.createPercentValue(100));
        
        // Add headers
        String[] headers = {"Location", "Type", "Temp (°C)", "Humidity %", "Condition", "Date Range", "Country"};
        for (String header : headers) {
            table.addHeaderCell(new Cell().add(new Paragraph(header).setBold()));
        }
        
        // Add data rows
        for (WeatherResponseDTO record : records) {
            table.addCell(new Cell().add(new Paragraph(record.getLocationName() != null ? record.getLocationName() : "-")));
            table.addCell(new Cell().add(new Paragraph(record.getLocationType() != null ? record.getLocationType() : "-")));
            table.addCell(new Cell().add(new Paragraph(record.getTemperature() != null ? 
                    String.format("%.1f", record.getTemperature()) : "-")));
            table.addCell(new Cell().add(new Paragraph(record.getHumidity() != null ? 
                    record.getHumidity().toString() : "-")));
            table.addCell(new Cell().add(new Paragraph(record.getWeatherCondition() != null ? 
                    record.getWeatherCondition() : "-")));
            table.addCell(new Cell().add(new Paragraph(
                    (record.getStartDate() != null ? record.getStartDate().toString() : "-") + " to " +
                    (record.getEndDate() != null ? record.getEndDate().toString() : "-"))));
            table.addCell(new Cell().add(new Paragraph(record.getCountry() != null ? record.getCountry() : "-")));
        }
        
        document.add(table);
        document.close();
        
        return baos.toByteArray();
    }

    /**
     * Wrapper class for XML root element
     */
    @lombok.Data
    @lombok.AllArgsConstructor
    @lombok.NoArgsConstructor
    private static class WeatherRecordsWrapper {
        private List<WeatherResponseDTO> weatherRecords;
    }
}

