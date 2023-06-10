package dev.psulej.dataintegrationprojectbackend.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;


@Entity
@Table(name = "weather_data")
@Builder
@Setter(AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SequenceGenerator(name = "weatherDataIdSeq", sequenceName = "weather_data_seq", initialValue = 1, allocationSize = 50)
public class WeatherData {

        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "weatherDataIdSeq")
        private Long id;

        @Column(name = "weather_data_date")
        private Instant date;

        @Column(name = "temperature")
        private float temperature;

        @Column(name = "pressure")
        private float pressure;

        @Column(name = "wind_velocity")
        private float windVelocity;

        @Column(name = "wind_direction")
        private float windDirection;

        @Column(name = "precipitation")
        private float precipitation;
}

