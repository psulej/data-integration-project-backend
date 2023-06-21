package dev.psulej.dataintegrationprojectbackend.weather.convert;

import lombok.Builder;

import java.time.Instant;

@Builder
public record WeatherDataRow(
        Instant date,
        Float temperature,
        Float pressure,
        Float windVelocity,
        Float windDirection,
        Float precipitation
) {
}