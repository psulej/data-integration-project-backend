package dev.psulej.dataintegrationprojectbackend.mortality.repository;

import dev.psulej.dataintegrationprojectbackend.mortality.domain.MortalityData;
import dev.psulej.dataintegrationprojectbackend.weather.domain.WeatherData;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MortalityDataRepository extends JpaRepository<MortalityData, Long> {
    @Modifying
    @Query(value = "TRUNCATE TABLE mortality_data", nativeQuery = true)
    void truncateTable();

    @Query("SELECT wd FROM MortalityData wd")
    Slice<MortalityData> findSlice(Pageable pageable);
}
