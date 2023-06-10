package dev.psulej.dataintegrationprojectbackend.repository;
import dev.psulej.dataintegrationprojectbackend.domain.WeatherData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface WeatherDataRepository extends JpaRepository<WeatherData, UUID> {
}
