package dev.psulej.dataintegrationprojectbackend.summary;

import java.time.YearMonth;

public interface MortalityDataYearMonthSummaryProjection {
    String getYearMonth();
    Integer getTotalDeaths();
    Integer getManTotalDeaths();
    Integer getWomanTotalDeaths();
    Integer getOver65AgeDeaths();
    Integer getUnder65AgeDeaths();
}
