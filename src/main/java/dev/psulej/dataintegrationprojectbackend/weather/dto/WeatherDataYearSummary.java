package dev.psulej.dataintegrationprojectbackend.weather.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class WeatherDataYearSummary {

    private int year;

    private float averageTemperature;

    private float averagePressure;

    private float averageWindVelocity;

    private float averagePrecipitation;
}
