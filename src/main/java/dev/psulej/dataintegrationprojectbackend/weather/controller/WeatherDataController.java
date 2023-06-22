package dev.psulej.dataintegrationprojectbackend.weather.controller;

import dev.psulej.dataintegrationprojectbackend.weather.convert.WeatherDataJsonExporter;
import dev.psulej.dataintegrationprojectbackend.weather.convert.WeatherDataJsonImporter;
import dev.psulej.dataintegrationprojectbackend.weather.convert.WeatherDataXmlExporter;
import dev.psulej.dataintegrationprojectbackend.weather.convert.WeatherDataXmlImporter;
import dev.psulej.dataintegrationprojectbackend.weather.domain.WeatherData;
import dev.psulej.dataintegrationprojectbackend.weather.service.WeatherDataService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
@RestController
@RequestMapping("/data/weather")
@AllArgsConstructor
public class WeatherDataController {

    private final WeatherDataService weatherDataService;
    private final WeatherDataXmlExporter weatherDataXmlExporter;
    private final WeatherDataJsonExporter weatherDataJsonExporter;
    private final WeatherDataJsonImporter weatherDataJsonImporter;
    private final WeatherDataXmlImporter weatherDataXmlImporter;


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

    @PostMapping("/import/json")
    public ResponseEntity<String> importJsonWeatherData(@RequestParam("file") MultipartFile file) {
        try {
            log.info("Import File {}", file.getOriginalFilename());
            InputStream fileStream = file.getInputStream();
             weatherDataJsonImporter.importData(fileStream);
            return new ResponseEntity<>("JSON import successful", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Import failed: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/import/xml")
    public ResponseEntity<String> importXmlWeatherData(@RequestParam("file") MultipartFile file) {
        try {
            log.info("Import File {}", file.getOriginalFilename());
            InputStream fileStream = file.getInputStream();
            weatherDataXmlImporter.importData(fileStream);
            return new ResponseEntity<>("XML import successful", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Import failed: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteWeatherData() {
        weatherDataService.deleteWeatherData();
    }
}