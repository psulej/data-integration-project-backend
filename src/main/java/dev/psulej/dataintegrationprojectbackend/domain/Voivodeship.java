package dev.psulej.dataintegrationprojectbackend.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "voivodeship")
@Setter(AccessLevel.PRIVATE)
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SequenceGenerator(name = "voivodeshipIdSeq", sequenceName = "voivodeship_seq", initialValue = 1, allocationSize = 50)
public class Voivodeship {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "voivodeshipIdSeq")
    private Long id;

    @Column(name = "voivodeship_name", unique = true)
    String name;

    public static Voivodeship fromName(String name) {
        return new Voivodeship(null,name);
    }
}