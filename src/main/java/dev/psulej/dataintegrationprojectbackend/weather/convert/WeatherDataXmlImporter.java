package dev.psulej.dataintegrationprojectbackend.weather.convert;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import dev.psulej.dataintegrationprojectbackend.common.parser.ObjectMapperProvider;
import dev.psulej.dataintegrationprojectbackend.weather.domain.WeatherData;
import dev.psulej.dataintegrationprojectbackend.weather.repository.WeatherDataRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
public class WeatherDataXmlImporter {

    private static final int BATCH_SIZE = 500;

    private final ObjectMapperProvider objectMapperProvider;
    private final WeatherDataRepository weatherDataRepository;

    @Transactional
    public void importData(InputStream inputStream) {
        try {
            XmlMapper mapper = objectMapperProvider.xmlObjectMapper();
            XMLInputFactory factory = XMLInputFactory.newFactory();
            XMLStreamReader reader = factory.createXMLStreamReader(inputStream);

            List<WeatherData> batch = new ArrayList<>(BATCH_SIZE);

            while (reader.hasNext()) {
                int event = reader.next();
                if (event == XMLStreamReader.START_ELEMENT && reader.getLocalName().equals("WeatherDataRow")) {
                    WeatherDataRow weatherDataRow = mapper.readValue(reader, WeatherDataRow.class);

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
            }

            if (!batch.isEmpty()) {
                saveBatch(batch);
            }

            log.info("WeatherData has been imported");
        } catch (IOException | XMLStreamException e) {
            log.error("Error during importing weather data xml file", e);
            throw new RuntimeException(e);
        }
    }

    private void saveBatch(List<WeatherData> batch) {
        weatherDataRepository.saveAll(batch);
        log.info("Batch of {} WeatherData items have been saved", batch.size());
    }

}
