package dev.psulej.dataintegrationprojectbackend.summary;

public interface WeatherDataYearMonthSummaryProjection {
    String getYearMonth();
    Float getAverageTemperature();
    Float getAveragePressure();
    Float getAveragePrecipitation();
    Float getAverageWindVelocity();
}
