package dev.psulej.dataintegrationprojectbackend.soap.weather.controller;

import dev.psulej.dataintegrationprojectbackend.GetWeatherDataRequest;
import dev.psulej.dataintegrationprojectbackend.GetWeatherDataResponse;
import dev.psulej.dataintegrationprojectbackend.WeatherData;
import dev.psulej.dataintegrationprojectbackend.repository.WeatherDataRepository;
import dev.psulej.dataintegrationprojectbackend.soap.dto.WeatherDataSummary;
import jakarta.persistence.Tuple;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Endpoint
public class WeatherEndpoint {

    private static final String NAMESPACE_URI = "http://www.psulej.dev/dataintegrationprojectbackend";

    private final WeatherDataRepository weatherDataRepository;

    @Autowired
    public WeatherEndpoint(WeatherDataRepository weatherDataRepository) {
        this.weatherDataRepository = weatherDataRepository;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getWeatherDataRequest")
    @ResponsePayload
    public GetWeatherDataResponse getYearlyAverageWeatherData(@RequestPayload GetWeatherDataRequest request) {
        GetWeatherDataResponse response = new GetWeatherDataResponse();

        List<WeatherDataSummary> weatherDataList = weatherDataRepository.getYearlyAverageWeatherData()

                .stream().map(tuple ->
                                WeatherDataSummary.builder()
                                        .year(tuple.get("year", BigDecimal.class).intValue())
                                        .averageTemperature(tuple.get("temperature", Double.class).floatValue())
                                        .averagePressure(tuple.get("pressure", Double.class).floatValue())
                                        .averageWindVelocity(tuple.get("wind_velocity", Double.class).floatValue())
                                        .averagePrecipitation(tuple.get("precipitation", Double.class).floatValue())
                                        .build()
                ).toList();

//        for (weatherData:weatherDataList
//             ) {
//
//        }
        if (!weatherDataList.isEmpty()) {
            WeatherDataSummary weatherDataSummary = weatherDataList.get(0);
            WeatherData averageWeatherData = new WeatherData();
            averageWeatherData.setAverageTemperature(weatherDataSummary.getAverageTemperature());
            averageWeatherData.setAveragePressure(weatherDataSummary.getAveragePressure());
            averageWeatherData.setAverageWindVelocity(weatherDataSummary.getAverageWindVelocity());
            averageWeatherData.setAveragePrecipitation(weatherDataSummary.getAveragePrecipitation());
            response.setAverageWeatherData(averageWeatherData);
        } else {
            log.info("Average weather data error");
        }

        return response;
    }

}

