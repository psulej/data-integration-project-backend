package dev.psulej.dataintegrationprojectbackend.user.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record LoginRequest(
        @Pattern(message = "Login is invalid", regexp = "[A-Za-z0-9]{3,14}")
        @NotBlank
        String login,

        @Pattern(message = "Password is invalid", regexp = "[A-Za-z0-9]{5,14}")
        @NotBlank
        String password
) {
}
