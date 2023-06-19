package dev.psulej.dataintegrationprojectbackend.weather.export;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import dev.psulej.dataintegrationprojectbackend.weather.domain.WeatherData;
import dev.psulej.dataintegrationprojectbackend.weather.repository.WeatherDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import java.io.*;
import java.text.SimpleDateFormat;

@Component
@RequiredArgsConstructor
@Slf4j
public class WeatherDataJsonExporter{

    private static final int BATCH_SIZE = 500;

    private final WeatherDataRepository weatherDataRepository;

    public void export(OutputStream outputStream) {
        try  {
            ObjectMapper mapper = new ObjectMapper()
                    .findAndRegisterModules()
                    .enable(SerializationFeature.INDENT_OUTPUT)
                    .setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm"));

            JsonFactory factory = mapper.getFactory();
            JsonGenerator jsonGenerator = factory.createGenerator(outputStream)
                    .useDefaultPrettyPrinter();
            jsonGenerator.writeStartArray();

            PageRequest pageable = PageRequest.of(0, BATCH_SIZE);
            Slice<WeatherData> slice = weatherDataRepository.findSlice(pageable);
            do {
                slice.getContent().forEach(weatherData -> {
                    try {
                        jsonGenerator.writeObject(weatherData);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
                log.info("WeatherData slice of {} items have been written to output stream", slice.getContent().size());
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
