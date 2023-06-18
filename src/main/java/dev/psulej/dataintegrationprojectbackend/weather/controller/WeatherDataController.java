package dev.psulej.dataintegrationprojectbackend.weather.controller;

import dev.psulej.dataintegrationprojectbackend.weather.domain.WeatherData;
import dev.psulej.dataintegrationprojectbackend.weather.service.WeatherDataService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/data/weather")
@AllArgsConstructor
public class WeatherDataController {

    private final WeatherDataService weatherDataService;

    @GetMapping
    public Page<WeatherData> getWeatherData(Pageable pageable) {
        return weatherDataService.getWeatherData(pageable);
    }

}
