package dev.psulej.dataintegrationprojectbackend.weather.convert;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.psulej.dataintegrationprojectbackend.common.parser.ObjectMapperProvider;
import dev.psulej.dataintegrationprojectbackend.weather.domain.WeatherData;
import dev.psulej.dataintegrationprojectbackend.weather.repository.WeatherDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class WeatherDataJsonImporter {

    private static final int BATCH_SIZE = 500;

    private final ObjectMapperProvider objectMapperProvider;
    private final WeatherDataRepository weatherDataRepository;


    public void importData(InputStream inputStream) {
        try {
            ObjectMapper mapper = objectMapperProvider.jsonObjectMapper();

            JsonFactory factory = mapper.getFactory();
            JsonParser jsonParser = factory.createParser(inputStream);

            if (jsonParser.nextToken() != JsonToken.START_ARRAY) {
                throw new IllegalStateException("Expected an array");
            }

            List<WeatherData> batch = new ArrayList<>(BATCH_SIZE);

            while (jsonParser.nextToken() == JsonToken.START_OBJECT) {
                WeatherDataRow weatherDataRow = mapper.readValue(jsonParser, WeatherDataRow.class);

                WeatherData weatherData = WeatherData.builder()
                        .date(weatherDataRow.date())
                        .temperature(weatherDataRow.temperature())
                        .pressure(weatherDataRow.pressure())
                        .windVelocity(weatherDataRow.windVelocity())
                        .windDirection(weatherDataRow.windDirection())
                        .precipitation(weatherDataRow.precipitation())
                        .build();

                batch.add(weatherData);

                if (batch.size() >= BATCH_SIZE) {
                    saveBatch(batch);
                    batch.clear();
                }
            }

            if (!batch.isEmpty()) {
                saveBatch(batch);
            }

            log.info("WeatherData has been imported");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveBatch(List<WeatherData> batch) {
        weatherDataRepository.saveAll(batch);
        log.info("Batch of {} WeatherData items have been saved", batch.size());
    }
}

