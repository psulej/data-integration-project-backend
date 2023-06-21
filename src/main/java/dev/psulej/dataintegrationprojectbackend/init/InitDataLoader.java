package dev.psulej.dataintegrationprojectbackend.init;
import dev.psulej.dataintegrationprojectbackend.mortality.init.MortalityDataInitializer;
import dev.psulej.dataintegrationprojectbackend.weather.init.WeatherDataInitializer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class InitDataLoader implements CommandLineRunner {

    private final WeatherDataInitializer weatherDataInitializer;
    private final MortalityDataInitializer mortalityDataInitializer;

    @Override
    public void run(String... args) throws Exception {
        weatherDataInitializer.initializeWeatherDataFromFile();
        mortalityDataInitializer.initializeMortalityDataFromFile();
    }
}
