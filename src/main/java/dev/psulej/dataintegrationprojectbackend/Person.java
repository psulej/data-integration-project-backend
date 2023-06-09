package dev.psulej.dataintegrationprojectbackend;

import java.util.Date;

public record Person(
        Boolean isElderly,
        Gender gender,
        Date dateOfDeath,
        Voivodeship voivodeship
) {
}
