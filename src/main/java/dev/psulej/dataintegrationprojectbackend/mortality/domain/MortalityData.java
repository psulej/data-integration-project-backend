package dev.psulej.dataintegrationprojectbackend.mortality.domain;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "mortality_data")
@Builder
@Setter(AccessLevel.PRIVATE)
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SequenceGenerator(name = "mortalityDataIdSeq", sequenceName = "mortality_data_seq", initialValue = 1, allocationSize = 50)
public class MortalityData {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mortalityDataIdSeq")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "voivodeship_id", unique = false, referencedColumnName = "id")
    Voivodeship voivodeship;

    @Column(name = "mortality_data_date")
    private Instant date;

//    @Column(name = "year")
//    int year;
//
//    @Column(name = "month_number")
//    int monthNumber;

    @Column(name = "woman_under_65_age")
    int womanUnder65Age;

    @Column(name = "woman_over_65_age")
    int womanOver65Age;

    @Column(name = "man_under_65_age")
    int manUnder65Age;

    @Column(name = "man_over_65_age")
    int manOver65Age;
}

