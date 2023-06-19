package dev.psulej.dataintegrationprojectbackend.summary;

import java.time.YearMonth;

public interface WeatherDataYearMonthSummaryProjection {
    String getYearMonth();
    Float getAverageTemperature();
    Float getAveragePrecipitation();
    Float getAverageWindVelocity();
}
