package dev.psulej.dataintegrationprojectbackend.init;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class InitDataReader implements CommandLineRunner {

    private final WeatherDataInitializer weatherDataInitializer;
    private final MortalityDataInitializer mortalityDataInitializer;

    @Override
    public void run(String... args) throws Exception {
        weatherDataInitializer.initializeWeatherDataFromFile();
        mortalityDataInitializer.initializeMortalityDataFromFile();
    }
}
