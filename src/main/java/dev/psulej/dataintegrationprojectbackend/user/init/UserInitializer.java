package dev.psulej.dataintegrationprojectbackend.user.init;

import dev.psulej.dataintegrationprojectbackend.user.domain.User;
import dev.psulej.dataintegrationprojectbackend.user.domain.UserRole;
import dev.psulej.dataintegrationprojectbackend.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserInitializer implements CommandLineRunner {

    UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if(userRepository.count() == 0) {
            User user = User.builder()
                    .id(null)
                    .email("useruser@wp.pl")
                    .login("user123")
                    .name("User")
                    .password(passwordEncoder.encode("User123"))
                    .role(UserRole.USER)
                    .build();

            User admin = User.builder()
                    .id(null)
                    .email("adminadmin@wp.pl")
                    .login("admin123")
                    .name("Admin")
                    .password(passwordEncoder.encode("Admin123"))
                    .role(UserRole.ADMIN)
                    .build();

            userRepository.save(user);
            userRepository.save(admin);
        }
    }
}
