package dev.psulej.dataintegrationprojectbackend.weather.repository;

public interface WeatherDataSummaryProjection {

    int getYear();

    Float getAverageTemperature();

    Float getAveragePressure();

    Float getAveragePrecipitation();

    Float getAverageWindVelocity();
}