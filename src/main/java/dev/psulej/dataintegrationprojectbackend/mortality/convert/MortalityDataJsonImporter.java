package dev.psulej.dataintegrationprojectbackend.mortality.convert;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.psulej.dataintegrationprojectbackend.common.parser.ObjectMapperProvider;
import dev.psulej.dataintegrationprojectbackend.mortality.domain.MortalityData;
import dev.psulej.dataintegrationprojectbackend.mortality.domain.Voivodeship;
import dev.psulej.dataintegrationprojectbackend.mortality.repository.MortalityDataRepository;
import dev.psulej.dataintegrationprojectbackend.mortality.repository.VoivodeshipRepository;
import dev.psulej.dataintegrationprojectbackend.weather.convert.WeatherDataRow;
import dev.psulej.dataintegrationprojectbackend.weather.domain.WeatherData;
import dev.psulej.dataintegrationprojectbackend.weather.repository.WeatherDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class MortalityDataJsonImporter {

    private static final int BATCH_SIZE = 500;

    private final ObjectMapperProvider objectMapperProvider;
    private final MortalityDataRepository mortalityDataRepository;
    private final VoivodeshipRepository voivodeshipRepository;


    public void importData(InputStream inputStream) {
        try {
            ObjectMapper mapper = objectMapperProvider.jsonObjectMapper();

            JsonFactory factory = mapper.getFactory();
            JsonParser jsonParser = factory.createParser(inputStream);

            if (jsonParser.nextToken() != JsonToken.START_ARRAY) {
                throw new IllegalStateException("Expected an array");
            }

            List<MortalityData> batch = new ArrayList<>(BATCH_SIZE);

            List<Voivodeship> voivodeships = voivodeshipRepository.findAll();
            Map<String, Voivodeship> voivodeshipByName = voivodeships.stream()
                    .collect(Collectors.toMap(Voivodeship::getName, Function.identity()));

            while (jsonParser.nextToken() == JsonToken.START_OBJECT) {
                MortalityDataRow mortalityDataRow = mapper.readValue(jsonParser, MortalityDataRow.class);

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

            if (!batch.isEmpty()) {
                saveBatch(batch);
            }

            log.info("WeatherData has been imported");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveBatch(List<MortalityData> batch) {
        mortalityDataRepository.saveAll(batch);
        log.info("Batch of {} WeatherData items have been saved", batch.size());
    }
}

