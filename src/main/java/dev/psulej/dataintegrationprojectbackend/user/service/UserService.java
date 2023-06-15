package dev.psulej.dataintegrationprojectbackend.user.service;

import dev.psulej.dataintegrationprojectbackend.security.ApplicationUserDetails;
import dev.psulej.dataintegrationprojectbackend.security.TokenProvider;
import dev.psulej.dataintegrationprojectbackend.user.api.RegisterRequest;
import dev.psulej.dataintegrationprojectbackend.user.domain.User;
import dev.psulej.dataintegrationprojectbackend.user.domain.UserRole;
import dev.psulej.dataintegrationprojectbackend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;
    private final UserValidator userValidator;


    public User getLoggedUser() {
        String login = Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getPrincipal)
                .filter(principal -> principal.getClass().isAssignableFrom(ApplicationUserDetails.class))
                .map(principal -> (ApplicationUserDetails) principal)
                .map(ApplicationUserDetails::getLogin)
                .orElseThrow(() -> new IllegalStateException("User cannot be extracted"));
        return findUserByLogin(login)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public Optional<User> findUserByLogin(String login) {
        return userRepository.findByLoginIgnoreCase(login);
    }

    public void register(RegisterRequest registerRequest) {
        Optional<User> t = Optional.empty();

        userValidator.validate(registerRequest);

        User registerUser = User.builder()
                .login(registerRequest.login())
                .password(passwordEncoder.encode(registerRequest.password()))
                .name(registerRequest.name())
                .email(registerRequest.email())
                .role(UserRole.USER)
                .build();

        userRepository.save(registerUser);
    }

    public String authenticateAndGetToken(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        return tokenProvider.generate(authentication);
    }
}
