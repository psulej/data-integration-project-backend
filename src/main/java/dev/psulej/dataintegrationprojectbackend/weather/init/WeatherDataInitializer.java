package dev.psulej.dataintegrationprojectbackend.weather.init;

import dev.psulej.dataintegrationprojectbackend.weather.domain.WeatherData;
import dev.psulej.dataintegrationprojectbackend.weather.repository.WeatherDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class WeatherDataInitializer {

    public static final int INSERT_BATCH_SIZE = 500;

    private final WeatherDataRepository weatherDataRepository;

    @Transactional
    public void initializeWeatherDataFromFile() throws FileNotFoundException {
        if(weatherDataRepository.count() == 0) {
            weatherDataRepository.truncateTable();
            readWeatherDataFromFile();
        }
    }

    private void readWeatherDataFromFile() throws FileNotFoundException {
        File csvFile = ResourceUtils.getFile("classpath:data/meteorological_data.csv");
        CSVFormat format = CSVFormat.DEFAULT
                .withDelimiter(',')
                .withHeader("Date", "Temperature", "Pressure", "WindVelocity", "WindDirection", "Precipitation")
                .withSkipHeaderRecord();
        try (Reader reader = new FileReader(csvFile);
             CSVParser csvParser = new CSVParser(reader, format)) {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm");
            List<WeatherData> weatherDataList = new ArrayList<>();

            for (CSVRecord csvRecord : csvParser) {
                String date = csvRecord.get(0);
                Date parsedDate = sdf.parse(date);
                String temperature = csvRecord.get(1);
                String pressure = csvRecord.get(2);
                String windVelocity = csvRecord.get(3);
                String windDirection = csvRecord.get(4);
                String precipitation = csvRecord.get(5);

                Instant weatherMeasurementTime = ZonedDateTime.ofInstant(parsedDate.toInstant(), ZoneOffset.UTC).toInstant();

                WeatherData weatherData = WeatherData.builder()
                        .date(weatherMeasurementTime)
                        .temperature(parseFloat(temperature))
                        .pressure(parseFloat(pressure))
                        .windVelocity(parseFloat(windVelocity))
                        .windDirection(parseFloat(windDirection))
                        .precipitation(parseFloat(precipitation))
                        .build();

                weatherDataList.add(weatherData);

                if (weatherDataList.size() == INSERT_BATCH_SIZE) {
                    weatherDataRepository.saveAll(weatherDataList);
                    weatherDataRepository.flush();
                    log.info("Weather data batch persisted (size = {})", INSERT_BATCH_SIZE);
                    weatherDataList.clear();
                }
            }

            if (weatherDataList.size() > 0) {
                weatherDataRepository.saveAll(weatherDataList);
                log.info("Weather data batch persisted (size = {})", weatherDataList.size());
            }

            log.info("Weather data file has been processed");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private Float parseFloat(String value) {
        return Optional.ofNullable(value)
                .filter(StringUtils::hasText)
                .map(Float::parseFloat)
                .orElse(null);
    }
}
