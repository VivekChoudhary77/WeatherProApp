package com.weatherpro.repository;

import com.weatherpro.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for Location entity
 */
@Repository
public interface LocationRepository extends JpaRepository<Location, UUID> {

    /**
     * Find location by name (case-insensitive)
     */
    Optional<Location> findByLocationNameIgnoreCase(String locationName);

    /**
     * Find all validated locations
     */
    List<Location> findByValidatedTrue();

    /**
     * Find locations by type
     */
    List<Location> findByLocationType(String locationType);
}

