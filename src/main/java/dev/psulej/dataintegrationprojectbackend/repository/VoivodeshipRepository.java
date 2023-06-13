package dev.psulej.dataintegrationprojectbackend.repository;

import dev.psulej.dataintegrationprojectbackend.domain.Voivodeship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoivodeshipRepository extends JpaRepository<Voivodeship, Long> {
}
