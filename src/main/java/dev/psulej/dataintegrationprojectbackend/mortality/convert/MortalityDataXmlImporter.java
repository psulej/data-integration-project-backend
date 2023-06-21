package dev.psulej.dataintegrationprojectbackend.mortality.convert;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import dev.psulej.dataintegrationprojectbackend.common.parser.ObjectMapperProvider;
import dev.psulej.dataintegrationprojectbackend.mortality.domain.MortalityData;
import dev.psulej.dataintegrationprojectbackend.mortality.domain.Voivodeship;
import dev.psulej.dataintegrationprojectbackend.mortality.repository.MortalityDataRepository;
import dev.psulej.dataintegrationprojectbackend.mortality.repository.VoivodeshipRepository;
import dev.psulej.dataintegrationprojectbackend.weather.convert.WeatherDataRow;
import dev.psulej.dataintegrationprojectbackend.weather.domain.WeatherData;
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
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
@AllArgsConstructor
public class MortalityDataXmlImporter {

    private static final int BATCH_SIZE = 500;

    private final ObjectMapperProvider objectMapperProvider;
    private final MortalityDataRepository mortalityDataRepository;
    private final VoivodeshipRepository voivodeshipRepository;

    @Transactional
    public void importData(InputStream inputStream) {
        try {
            XmlMapper mapper = objectMapperProvider.xmlObjectMapper();
            XMLInputFactory factory = XMLInputFactory.newFactory();
            XMLStreamReader reader = factory.createXMLStreamReader(inputStream);

            List<MortalityData> batch = new ArrayList<>(BATCH_SIZE);

            List<Voivodeship> voivodeships = voivodeshipRepository.findAll();
            Map<String, Voivodeship> voivodeshipByName = voivodeships.stream()
                    .collect(Collectors.toMap(Voivodeship::getName, Function.identity()));

            while (reader.hasNext()) {
                int event = reader.next();
                if (event == XMLStreamReader.START_ELEMENT && reader.getLocalName().equals("MortalityDataRow")) {

                    MortalityDataRow mortalityDataRow = mapper.readValue(reader, MortalityDataRow.class);

                    MortalityData mortalityData = MortalityData.builder()
                            .womanUnder65Age(mortalityDataRow.womanUnder65Age())
                            .womanOver65Age(mortalityDataRow.womanOver65Age())
                            .manOver65Age(mortalityDataRow.manOver65Age())
                            .manUnder65Age(mortalityDataRow.manUnder65Age())
                            .date(mortalityDataRow.date())
                            .voivodeship(voivodeshipByName.get(mortalityDataRow.voivodeshipName()))
                            .build();

                    batch.add(mortalityData);

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

    private void saveBatch(List<MortalityData> batch) {
        mortalityDataRepository.saveAll(batch);
        log.info("Batch of {} WeatherData items have been saved", batch.size());
    }

}
