package dev.psulej.dataintegrationprojectbackend.mortality.export;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import dev.psulej.dataintegrationprojectbackend.mortality.domain.MortalityData;
import dev.psulej.dataintegrationprojectbackend.mortality.repository.MortalityDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import java.io.*;
import java.text.SimpleDateFormat;

@Component
@RequiredArgsConstructor
@Slf4j
public class MortalityDataJsonExporter{

    private static final int BATCH_SIZE = 500;

    private final MortalityDataRepository mortalityDataRepository;

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
            Slice<MortalityData> slice = mortalityDataRepository.findSlice(pageable);
            do {
                slice.getContent().forEach(mortalityData -> {
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
