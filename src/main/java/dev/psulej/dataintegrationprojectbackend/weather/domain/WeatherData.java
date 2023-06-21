package dev.psulej.dataintegrationprojectbackend.weather.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;


@Entity
@Table(name = "weather_data")
@Builder
@Getter
@Setter(AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SequenceGenerator(name = "weatherDataIdSeq", sequenceName = "weather_data_seq", initialValue = 1, allocationSize = 500)
public class WeatherData {

        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "weatherDataIdSeq")
        private Long id;

        @Column(name = "weather_data_date")
        private Instant date;

        @Column(name = "temperature")
        private Float temperature;

        @Column(name = "pressure")
        private Float pressure;

        @Column(name = "wind_velocity")
        private Float windVelocity;

        @Column(name = "wind_direction")
        private Float windDirection;

        @Column(name = "precipitation")
        private Float precipitation;
}

