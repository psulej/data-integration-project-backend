package dev.psulej.dataintegrationprojectbackend.mortality.repository;

import dev.psulej.dataintegrationprojectbackend.mortality.domain.MortalityData;
import dev.psulej.dataintegrationprojectbackend.summary.MortalityDataYearMonthSummaryProjection;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MortalityDataRepository extends JpaRepository<MortalityData, Long> {
    @Modifying
    @Query(value = "TRUNCATE TABLE mortality_data", nativeQuery = true)
    void truncateTable();

    @Query("SELECT wd FROM MortalityData wd")
    Slice<MortalityData> findSlice(Pageable pageable);

    @Query(
            value = """
                    SELECT to_char(mortality_data_date, 'YYYY-MM') as YearMonth,
                           sum(mortality_data.woman_under_65_age + mortality_data.man_under_65_age) AS Under65AgeDeaths,
                           sum(mortality_data.woman_over_65_age + mortality_data.man_over_65_age) AS Over65AgeDeaths,
                           sum(mortality_data.woman_over_65_age + mortality_data.woman_under_65_age) AS WomanTotalDeaths,
                           sum(mortality_data.man_over_65_age + mortality_data.man_under_65_age) AS ManTotalDeaths,
                           sum(mortality_data.man_over_65_age + mortality_data.man_under_65_age + mortality_data.woman_over_65_age +
                               mortality_data.woman_under_65_age) AS TotalDeaths
                    FROM mortality_data
                    GROUP BY YearMonth
                    ORDER BY YearMonth desc;
                    """, nativeQuery = true
    )
    List<MortalityDataYearMonthSummaryProjection> getYearMonthSummary();
}
