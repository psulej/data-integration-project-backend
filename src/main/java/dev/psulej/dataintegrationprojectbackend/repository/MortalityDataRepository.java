package dev.psulej.dataintegrationprojectbackend.repository;

import dev.psulej.dataintegrationprojectbackend.domain.MortalityData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MortalityDataRepository extends JpaRepository<MortalityData, Long> {
    @Modifying
    @Query(value = "TRUNCATE TABLE mortality_data", nativeQuery = true)
    void truncateTable();
}
