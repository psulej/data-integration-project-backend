package dev.psulej.dataintegrationprojectbackend.weather.export;

import lombok.Builder;

import java.time.Instant;

@Builder
public record WeatherDataDto(
        Long id,
        Instant date,
        Float temperature,
        Float pressure,
        Float windVelocity,
        Float windDirection,
        Float precipitation
) {

}