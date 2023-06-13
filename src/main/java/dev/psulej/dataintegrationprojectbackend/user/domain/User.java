package dev.psulej.dataintegrationprojectbackend.user.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
@Entity
@Table(name = "users")
@Builder
@Setter(AccessLevel.PRIVATE)
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SequenceGenerator(name = "userIdSeq", sequenceName = "user_seq", initialValue = 1)
public class User {
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userIdSeq")
        private Long id;

        @Column(name="login")
        private String login;

        @Column(name="email")
        private String email;

        @Column(name="name")
        private String name;

        @Enumerated(EnumType.STRING)
        @Column(name="role")
        UserRole role;

        @Column(name="password")
        @JsonIgnore
        String password;
}


