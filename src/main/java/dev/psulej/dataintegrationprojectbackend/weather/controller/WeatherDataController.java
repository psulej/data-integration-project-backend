package dev.psulej.dataintegrationprojectbackend.weather.controller;

import dev.psulej.dataintegrationprojectbackend.weather.domain.WeatherData;
import dev.psulej.dataintegrationprojectbackend.weather.export.WeatherDataJsonExporter;
import dev.psulej.dataintegrationprojectbackend.weather.export.WeatherDataXmlExporter;
import dev.psulej.dataintegrationprojectbackend.weather.service.WeatherDataService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/data/weather")
@AllArgsConstructor
public class WeatherDataController {

    private final WeatherDataService weatherDataService;
    private final WeatherDataXmlExporter weatherDataXmlExporter;
    private final WeatherDataJsonExporter weatherDataJsonExporter;


    @GetMapping
    public Page<WeatherData> getWeatherData(Pageable pageable) {
        return weatherDataService.getWeatherData(pageable);
    }

    @GetMapping("/export/xml")
    public void exportXmlWeatherData(HttpServletResponse response) {
        try {
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=weather-data.xml");
            response.setContentType(MediaType.TEXT_HTML.toString());
            weatherDataXmlExporter.export(response.getOutputStream());
        } catch (IOException e) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    @GetMapping("/export/json")
    public void exportJsonWeatherData(HttpServletResponse response) {
        try {
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=weather-data.json");
            response.setContentType(MediaType.TEXT_HTML.toString());
            weatherDataJsonExporter.export(response.getOutputStream());
        } catch (IOException e) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }
}