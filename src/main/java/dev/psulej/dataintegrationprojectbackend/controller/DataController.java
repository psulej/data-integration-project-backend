package dev.psulej.dataintegrationprojectbackend.controller;

import dev.psulej.dataintegrationprojectbackend.domain.MortalityData;
import dev.psulej.dataintegrationprojectbackend.domain.WeatherData;
import dev.psulej.dataintegrationprojectbackend.service.MortalityDataService;
import dev.psulej.dataintegrationprojectbackend.service.WeatherDataService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/data")
@AllArgsConstructor
public class DataController {

    private final WeatherDataService weatherDataService;
    private final MortalityDataService mortalityDataService;

    @GetMapping("/weather-data")
    public Page<WeatherData> getWeatherData(Pageable pageable) {
        return weatherDataService.getWeatherData(pageable);
    }

    @GetMapping("/mortality-data")
    public Page<MortalityData> getMortalityData(Pageable pageable) {
        return mortalityDataService.getMortalityData(pageable);
    }

//    @GetMapping("/agregated-data")
//    public Page
}
