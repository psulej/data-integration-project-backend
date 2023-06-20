package dev.psulej.dataintegrationprojectbackend.summary;

import dev.psulej.dataintegrationprojectbackend.mortality.repository.MortalityDataRepository;
import dev.psulej.dataintegrationprojectbackend.weather.repository.WeatherDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DataSummaryService {

    private final WeatherDataRepository weatherDataRepository;
    private final MortalityDataRepository mortalityDataRepository;

    public List<DataSummary> getSummary() {
        Map<String, WeatherDataYearMonthSummaryProjection> weatherDataByYearMonth = weatherDataRepository.getYearMonthSummary()
                .stream().collect(Collectors.toMap(
                        WeatherDataYearMonthSummaryProjection::getYearMonth,
                        Function.identity()
                ));

        Map<String, MortalityDataYearMonthSummaryProjection> mortalityDataByYearMonth = mortalityDataRepository.getYearMonthSummary()
                .stream().collect(Collectors.toMap(
                        MortalityDataYearMonthSummaryProjection::getYearMonth,
                        Function.identity()
                ));

        Set<String> commonYearMonths = weatherDataByYearMonth.keySet().stream()
                .distinct()
                .sorted((o1, o2) -> -o1.compareTo(o2))
                .filter(mortalityDataByYearMonth::containsKey)
                .collect(Collectors.toCollection(LinkedHashSet::new));


        return commonYearMonths.stream()
                .map(yearMonth -> {
                    WeatherDataYearMonthSummaryProjection weatherDataYearMonthSummary = weatherDataByYearMonth.get(yearMonth);
                    MortalityDataYearMonthSummaryProjection mortalityDataYearMonthSummary = mortalityDataByYearMonth.get(yearMonth);

                    return DataSummary.builder()
                            .yearMonth(convertToYearMonth(yearMonth))
                            .averageTemperature(weatherDataYearMonthSummary.getAverageTemperature())
                            .averagePressure(weatherDataYearMonthSummary.getAveragePressure())
                            .averagePrecipitation(weatherDataYearMonthSummary.getAveragePrecipitation())
                            .averageWindVelocity(weatherDataYearMonthSummary.getAverageWindVelocity())
                            .under65AgeDeath(mortalityDataYearMonthSummary.getUnder65AgeDeaths())
                            .over65AgeDeaths(mortalityDataYearMonthSummary.getOver65AgeDeaths())
                            .manTotalDeaths(mortalityDataYearMonthSummary.getManTotalDeaths())
                            .womanTotalDeaths(mortalityDataYearMonthSummary.getWomanTotalDeaths())
                            .totalDeaths(mortalityDataYearMonthSummary.getTotalDeaths())
                            .build();
                })
                .collect(Collectors.toList());
    }

    private YearMonth convertToYearMonth(String rawYearMonth) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM");
        return YearMonth.parse(rawYearMonth, dateTimeFormatter);
    }

}
