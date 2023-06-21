package dev.psulej.dataintegrationprojectbackend.common.parser;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

@Component
public class ObjectMapperProvider {

    ObjectMapper jsonObjectMapper = new ObjectMapper()
            .findAndRegisterModules()
            .enable(SerializationFeature.INDENT_OUTPUT)
            .setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm"));

    XmlMapper xmlObjectMapper = (XmlMapper) new XmlMapper()
            .findAndRegisterModules()
            .enable(SerializationFeature.INDENT_OUTPUT)
            .setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm"));

    public ObjectMapper jsonObjectMapper() {
        return jsonObjectMapper;
    }

    public XmlMapper xmlObjectMapper() {
        return xmlObjectMapper;
    }
}
