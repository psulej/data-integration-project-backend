package dev.psulej.dataintegrationprojectbackend.repository;

import dev.psulej.dataintegrationprojectbackend.domain.WeatherData;
import dev.psulej.dataintegrationprojectbackend.soap.dto.WeatherDataSummary;
import jakarta.persistence.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;


@Repository
public interface WeatherDataRepository extends JpaRepository<WeatherData, Long> {

    @Modifying
    @Query(value = "TRUNCATE TABLE weather_data", nativeQuery = true)
    void truncateTable();

    Page<WeatherData> findByDateBetween(Instant dateFrom, Instant dateTo, Pageable pageable);

    @Query(
            value = """
            SELECT
                EXTRACT(YEAR FROM weather_data_date) as year,
                AVG(temperature) AS temperature,
                AVG(pressure) AS pressure,
                AVG(precipitation) AS precipitation,
                AVG(wind_velocity) AS wind_velocity
            FROM
                weather_data
            WHERE
                 EXTRACT(YEAR FROM weather_data_date) IN (2019, 2020)
            GROUP BY
                EXTRACT(YEAR FROM weather_data_date)
            ORDER BY
                year desc
            """,
            nativeQuery = true
    )
    List<Tuple> getYearlyAverageWeatherData();
}
