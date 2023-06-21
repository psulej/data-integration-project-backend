package dev.psulej.dataintegrationprojectbackend.mortality.convert;

import lombok.Builder;

import java.time.Instant;

@Builder
public record MortalityDataRow(

        String voivodeshipName,

        Instant date,

        int womanUnder65Age,

        int womanOver65Age,

        int manUnder65Age,

        int manOver65Age
) {
}
