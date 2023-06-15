package dev.psulej.dataintegrationprojectbackend.user.service;

import dev.psulej.dataintegrationprojectbackend.error.ApplicationError;
import dev.psulej.dataintegrationprojectbackend.error.ApplicationException;
import dev.psulej.dataintegrationprojectbackend.user.api.RegisterRequest;
import dev.psulej.dataintegrationprojectbackend.user.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserValidator {
    private final UserRepository userRepository;

    public UserValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    void validate(RegisterRequest request) {
        List<ApplicationError> errors = new ArrayList<>();

        if (userRepository.findByLoginIgnoreCase(request.login()).isPresent()) {
            errors.add(ApplicationError.LOGIN_EXISTS);
        }
        if (userRepository.findByEmailIgnoreCase(request.email()).isPresent()) {
            errors.add(ApplicationError.EMAIL_EXISTS);
        }
        if (!errors.isEmpty()) {
            throw new ApplicationException(errors);
        }
    }
}
