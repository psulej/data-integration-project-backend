package dev.psulej.dataintegrationprojectbackend.mortality.export;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import dev.psulej.dataintegrationprojectbackend.mortality.domain.MortalityData;
import dev.psulej.dataintegrationprojectbackend.mortality.domain.Voivodeship;
import dev.psulej.dataintegrationprojectbackend.mortality.repository.MortalityDataRepository;
import dev.psulej.dataintegrationprojectbackend.mortality.repository.VoivodeshipRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class MortalityDataJsonExporter {

    private static final int BATCH_SIZE = 500;

    private final MortalityDataRepository mortalityDataRepository;
    private final VoivodeshipRepository voivodeshipRepository;

    public void export(OutputStream outputStream) {
        try {
            ObjectMapper mapper = new ObjectMapper()
                    .findAndRegisterModules()
                    .enable(SerializationFeature.INDENT_OUTPUT)
                    .setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm"));

            JsonFactory factory = mapper.getFactory();
            JsonGenerator jsonGenerator = factory.createGenerator(outputStream)
                    .useDefaultPrettyPrinter();
            jsonGenerator.writeStartArray();

            List<Voivodeship> voivodeships = voivodeshipRepository.findAll();
            Map<Long, String> voivodeshipByName = voivodeships.stream()
                    .collect(Collectors.toMap(Voivodeship::getId, Voivodeship::getName));

            PageRequest pageable = PageRequest.of(0, BATCH_SIZE, Sort.by("date"));
            Slice<MortalityData> slice = mortalityDataRepository.findSlice(pageable);
            do {
                slice
                        .map(mortalityData -> MortalityDataRow.builder()
                                .womanUnder65Age(mortalityData.getWomanUnder65Age())
                                .womanOver65Age(mortalityData.getWomanOver65Age())
                                .manUnder65Age(mortalityData.getManUnder65Age())
                                .manOver65Age(mortalityData.getManOver65Age())
                                .date(mortalityData.getDate())
                                .voivodeshipName(voivodeshipByName.get(mortalityData.getVoivodeship().getId()))
                                .build())
                        .getContent()
                        .forEach(mortalityData -> {
                            try {
                                jsonGenerator.writeObject(mortalityData);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        });
                log.info("MortalityData slice of {} items have been written to output stream", slice.getContent().size());
                pageable = PageRequest.of(pageable.getPageNumber() + 1, BATCH_SIZE);
                slice = mortalityDataRepository.findSlice(pageable);
            } while (slice.hasNext());

            jsonGenerator.writeEndArray();
            jsonGenerator.close();
            jsonGenerator.flush();
            log.info("MortalityData has been exported");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
