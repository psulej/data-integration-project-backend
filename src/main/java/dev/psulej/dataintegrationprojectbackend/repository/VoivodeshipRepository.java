package dev.psulej.dataintegrationprojectbackend.repository;

import dev.psulej.dataintegrationprojectbackend.domain.Voivodeship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface VoivodeshipRepository extends JpaRepository<Voivodeship, Long> {
    @Modifying
    @Query(value = "TRUNCATE TABLE voivodeship", nativeQuery = true)
    void truncateTable();
}
