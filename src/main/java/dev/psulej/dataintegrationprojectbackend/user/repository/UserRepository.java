package dev.psulej.dataintegrationprojectbackend.user.repository;

import dev.psulej.dataintegrationprojectbackend.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLoginIgnoreCase(String login);
    Optional<User> findByEmailIgnoreCase(String email);
}
