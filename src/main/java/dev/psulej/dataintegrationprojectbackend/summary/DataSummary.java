package dev.psulej.dataintegrationprojectbackend.summary;

import lombok.Builder;

import java.time.YearMonth;

@Builder
public record DataSummary(
        YearMonth yearMonth,
        Float averageTemperature,
        Float averagePrecipitation,
        Float averageWindVelocity,
        Integer manTotalDeaths,
        Integer womanTotalDeaths,
        Integer over65AgeDeaths,
        Integer under65AgeDeath,
        Integer totalDeaths
) {
}
