package dev.psulej.dataintegrationprojectbackend.weather.service;

import dev.psulej.dataintegrationprojectbackend.user.service.UserService;
import dev.psulej.dataintegrationprojectbackend.weather.domain.WeatherData;
import dev.psulej.dataintegrationprojectbackend.weather.repository.WeatherDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;

@Service
@RequiredArgsConstructor
public class WeatherDataService {

    private final WeatherDataRepository weatherDataRepository;
    private final UserService userService;

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


    @Transactional
    public void deleteWeatherData() {
        weatherDataRepository.truncateTable();
    }
}
