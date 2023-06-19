package dev.psulej.dataintegrationprojectbackend.weather.export;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import dev.psulej.dataintegrationprojectbackend.weather.domain.WeatherData;
import dev.psulej.dataintegrationprojectbackend.weather.repository.WeatherDataRepository;
import jakarta.servlet.ServletOutputStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.*;
import java.text.SimpleDateFormat;

@Component
@RequiredArgsConstructor
@Slf4j
public class WeatherDataXmlExporter{

    private static final int BATCH_SIZE = 500;

    private final WeatherDataRepository weatherDataRepository;

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
            streamWriter.writeStartElement("WeatherDataList");

            PageRequest pageable = PageRequest.of(0, BATCH_SIZE);
            Slice<WeatherData> slice = weatherDataRepository.findSlice(pageable);
            do {
                slice.getContent().forEach(weatherData -> {
                    try {
                        mapper.writeValue(streamWriter, weatherData);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
                log.info("WeatherData slice of {} items have been written to output stream", slice.getContent().size());
                pageable = PageRequest.of(pageable.getPageNumber() + 1, BATCH_SIZE);
                slice = weatherDataRepository.findSlice(pageable);
            } while (slice.hasNext());

            // items - END
            streamWriter.writeEndElement();
            streamWriter.writeEndDocument();
            log.info("WeatherData has been exported");

            streamWriter.flush();
            streamWriter.close();
        } catch (XMLStreamException e) {
            throw new RuntimeException(e);
        }
    }
}


