//package dev.psulej.dataintegrationprojectbackend.weather.export;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.SerializationFeature;
//import dev.psulej.dataintegrationprojectbackend.weather.domain.WeatherData;
//import dev.psulej.dataintegrationprojectbackend.weather.repository.WeatherDataRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Slice;
//import org.springframework.stereotype.Component;
//
//import java.io.*;
//import java.text.SimpleDateFormat;
//
//@Component
//@RequiredArgsConstructor
//@Slf4j
//public class WeatherDataJsonExporter implements CommandLineRunner {
//
//    private static final int BATCH_SIZE = 500;
//
//    private final WeatherDataRepository weatherDataRepository;
//
//    @Override
//    public void run(String... args) throws Exception {
//        export();
//    }
//
//    public InputStream export() {
//        return new ByteArrayInputStream(new byte[]{});
//    }
//
//    private void export() {
//        try (OutputStream outStream = new FileOutputStream("weather-data.json", false)) {
//            ObjectMapper mapper = new ObjectMapper();
//            mapper.enable(SerializationFeature.INDENT_OUTPUT);
//            mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm"));
//
//            PageRequest pageable = PageRequest.of(0, BATCH_SIZE);
//            Slice<WeatherData> slice = weatherDataRepository.findSlice(pageable);
//            do {
//                slice.getContent().forEach(weatherData -> {
//                    try {
//                        String json = mapper.writeValueAsString(weatherData);
//                        outStream.write(json.getBytes());
//                        outStream.write("\n".getBytes());
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
//                });
//                log.info("WeatherData slice of {} items have been written to output stream", slice.getContent().size());
//                pageable = PageRequest.of(pageable.getPageNumber() + 1, BATCH_SIZE);
//                slice = weatherDataRepository.findSlice(pageable);
//            } while (slice.hasNext());
//
//            log.info("WeatherData has been exported");
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//}
