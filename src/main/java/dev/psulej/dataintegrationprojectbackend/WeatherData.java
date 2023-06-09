package dev.psulej.dataintegrationprojectbackend;

import java.util.Date;

public record WeatherData(
        Date date,
        Float temperature,
        Float pressure,
        Float windVelocity,
        Float precipitation
) {
}
