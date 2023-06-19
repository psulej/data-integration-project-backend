package dev.psulej.dataintegrationprojectbackend.mortality.export;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import dev.psulej.dataintegrationprojectbackend.mortality.domain.MortalityData;
import dev.psulej.dataintegrationprojectbackend.mortality.repository.MortalityDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;

@Component
@RequiredArgsConstructor
@Slf4j
public class MortalityDataXmlExporter{

    private static final int BATCH_SIZE = 500;

    private final MortalityDataRepository mortalityDataRepository;

    public void export(OutputStream outputStream) {
        try {
            XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newFactory();
            XMLStreamWriter streamWriter = xmlOutputFactory.createXMLStreamWriter(outputStream);
            XmlMapper mapper = (XmlMapper) new XmlMapper()
                    .findAndRegisterModules()
                    .enable(SerializationFeature.INDENT_OUTPUT)
                    .setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm"));

            streamWriter.writeStartDocument();
            // items - START
            streamWriter.writeStartElement("MortalityDataList");

            PageRequest pageable = PageRequest.of(0, BATCH_SIZE);
            Slice<MortalityData> slice = mortalityDataRepository.findSlice(pageable);
            do {
                slice.getContent().forEach(mortalityData -> {
                    try {
                        mapper.writeValue(streamWriter, mortalityData);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
                log.info("Mortality data slice of {} items have been written to output stream", slice.getContent().size());
                pageable = PageRequest.of(pageable.getPageNumber() + 1, BATCH_SIZE);
                slice = mortalityDataRepository.findSlice(pageable);
            } while (slice.hasNext());

            // items - END
            streamWriter.writeEndElement();
            streamWriter.writeEndDocument();
            log.info("Mortality has been exported");

            streamWriter.flush();
            streamWriter.close();
        } catch (XMLStreamException e) {
            throw new RuntimeException(e);
        }
    }
}
