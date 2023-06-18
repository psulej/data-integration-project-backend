package dev.psulej.dataintegrationprojectbackend.service;

import dev.psulej.dataintegrationprojectbackend.domain.WeatherData;
import dev.psulej.dataintegrationprojectbackend.repository.WeatherDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.*;

@Service
@RequiredArgsConstructor
public class WeatherDataService {

    private final WeatherDataRepository weatherDataRepository;

    public Page<WeatherData> getWeatherData(Pageable pageable) {
        Instant dateFrom = ZonedDateTime.of(
                LocalDate.of(2019, Month.JANUARY, 1).atStartOfDay(),
                ZoneOffset.UTC
        ).toInstant();
        Instant dateTo = ZonedDateTime.of(
                LocalDate.of(2020, Month.DECEMBER, 31).atStartOfDay(),
                ZoneOffset.UTC
        ).toInstant();


        return weatherDataRepository.findByDateBetween(dateFrom, dateTo, pageable);
    }
}
