package dev.psulej.dataintegrationprojectbackend.summary;

import java.time.YearMonth;

public interface WeatherDataYearMonthSummaryProjection {
    String getYearMonth();
    Float getAverageTemperature();
    Float getAveragePressure();
    Float getAveragePrecipitation();
    Float getAverageWindVelocity();
}
