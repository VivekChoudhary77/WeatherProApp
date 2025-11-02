package com.weatherpro.repository;

import com.weatherpro.model.WeatherRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Repository for WeatherRecord entity
 */
@Repository
public interface WeatherRecordRepository extends JpaRepository<WeatherRecord, UUID> {

    /**
     * Find weather records by location name (case-insensitive)
     */
    List<WeatherRecord> findByLocationNameContainingIgnoreCase(String locationName);

    /**
     * Find weather records by date range
     */
    List<WeatherRecord> findByStartDateBetween(LocalDate start, LocalDate end);

    /**
     * Find weather records by location and date range
     */
    @Query("SELECT w FROM WeatherRecord w WHERE " +
           "LOWER(w.locationName) LIKE LOWER(CONCAT('%', :location, '%')) " +
           "AND w.startDate >= :startDate AND w.endDate <= :endDate")
    List<WeatherRecord> findByLocationAndDateRange(
        @Param("location") String location,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );

    /**
     * Find all records ordered by creation date descending
     */
    List<WeatherRecord> findAllByOrderByCreatedAtDesc();

    /**
     * Find records by country
     */
    List<WeatherRecord> findByCountryIgnoreCase(String country);
    
    /**
     * Check if duplicate record exists (same location and overlapping date range)
     */
    @Query("SELECT COUNT(w) > 0 FROM WeatherRecord w WHERE " +
           "LOWER(w.locationName) = LOWER(:locationName) AND " +
           "((w.startDate <= :endDate AND w.endDate >= :startDate))")
    boolean existsDuplicateRecord(
        @Param("locationName") String locationName,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );
}

