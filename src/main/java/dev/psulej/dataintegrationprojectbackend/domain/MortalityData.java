package dev.psulej.dataintegrationprojectbackend.domain;
import lombok.Builder;

@Builder
public record MortalityData(
        String voivodeship,
        int year,
        int monthNumber,
        int womanUnder65,
        int womanAfter65,
        int manUnder65,
        int manAfter65
) {
}
