package dev.psulej.dataintegrationprojectbackend.repository;

import dev.psulej.dataintegrationprojectbackend.domain.WeatherData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;


@Repository
public interface WeatherDataRepository extends JpaRepository<WeatherData, Long> {

    @Modifying
    @Query(value = "TRUNCATE TABLE weather_data", nativeQuery = true)
    void truncateTable();

    Page<WeatherData> findByDateBetween(Instant dateFrom, Instant dateTo, Pageable pageable);
}
