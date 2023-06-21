package dev.psulej.dataintegrationprojectbackend.weather.convert;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.psulej.dataintegrationprojectbackend.common.parser.ObjectMapperProvider;
import dev.psulej.dataintegrationprojectbackend.weather.domain.WeatherData;
import dev.psulej.dataintegrationprojectbackend.weather.repository.WeatherDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;
@Component
@RequiredArgsConstructor
@Slf4j
public class WeatherDataJsonExporter {

    private static final int BATCH_SIZE = 500;

    private final ObjectMapperProvider objectMapperProvider;
    private final WeatherDataRepository weatherDataRepository;

    public void export(OutputStream outputStream) {
        try {
            ObjectMapper mapper = objectMapperProvider.jsonObjectMapper();

            JsonFactory factory = mapper.getFactory();
            JsonGenerator jsonGenerator = factory.createGenerator(outputStream)
                    .useDefaultPrettyPrinter();
            jsonGenerator.writeStartArray();

            PageRequest pageable = PageRequest.of(0, BATCH_SIZE);
            Slice<WeatherData> slice = weatherDataRepository.findSlice(pageable);

            do {
                slice
                        .map(weatherData -> WeatherDataRow.builder()
                                .date(weatherData.getDate())
                                .temperature(weatherData.getTemperature())
                                .pressure(weatherData.getPressure())
                                .windVelocity(weatherData.getWindVelocity())
                                .windDirection(weatherData.getWindDirection())
                                .precipitation(weatherData.getPrecipitation())
                        .build())
                        .getContent()
                        .forEach(weatherData -> {
                            try {
                                jsonGenerator.writeObject(weatherData);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        });

                log.info("Weather slice of {} items have been written to output stream", slice.getContent().size());
                pageable = PageRequest.of(pageable.getPageNumber() + 1, BATCH_SIZE);
                slice = weatherDataRepository.findSlice(pageable);
            } while (slice.hasNext());

            jsonGenerator.writeEndArray();
            jsonGenerator.close();
            jsonGenerator.flush();
            log.info("WeatherData has been exported");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
