package dev.psulej.dataintegrationprojectbackend.weather.endpoint;

import dev.psulej.dataintegrationprojectbackend.GetWeatherDataRequest;
import dev.psulej.dataintegrationprojectbackend.GetWeatherDataResponse;
import dev.psulej.dataintegrationprojectbackend.WeatherData;
import dev.psulej.dataintegrationprojectbackend.weather.repository.WeatherDataRepository;
import dev.psulej.dataintegrationprojectbackend.weather.repository.WeatherDataSummaryProjection;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.util.ArrayList;
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

        List<WeatherDataSummaryProjection> weatherDataList = weatherDataRepository.getYearlyAverageWeatherData();

        if (!weatherDataList.isEmpty()) {
            List<WeatherData> averageWeatherDataList = new ArrayList<>();

            for (WeatherDataSummaryProjection weatherDataSummary : weatherDataList) {
                WeatherData averageWeatherData = new WeatherData();
                averageWeatherData.setYear(weatherDataSummary.getYear());
                averageWeatherData.setAverageTemperature(weatherDataSummary.getAverageTemperature());
                averageWeatherData.setAveragePressure(weatherDataSummary.getAveragePressure());
                averageWeatherData.setAverageWindVelocity(weatherDataSummary.getAverageWindVelocity());
                averageWeatherData.setAveragePrecipitation(weatherDataSummary.getAveragePrecipitation());
                averageWeatherDataList.add(averageWeatherData);
            }

            response.getAverageWeatherData().addAll(averageWeatherDataList);
        } else {
            log.info("Average weather data error");
        }

        return response;
    }

}

