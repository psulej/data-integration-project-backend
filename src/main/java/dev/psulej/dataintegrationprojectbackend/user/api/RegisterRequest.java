//package dev.psulej.dataintegrationprojectbackend.user.api;
//
//import jakarta.validation.constraints.Email;
//import jakarta.validation.constraints.NotBlank;
//import jakarta.validation.constraints.Pattern;
//import lombok.Builder;
//
//@Builder
//public record RegisterRequest(
//        @Pattern(message = "Register Login is invalid", regexp = "[A-Za-z0-9]{3,14}")
//        @NotBlank
//        String login,
//
//        @Pattern(message = "Register Password is invalid", regexp = "[A-Za-z0-9]{5,14}")
//        @NotBlank
//        String password,
//
//        @Pattern(message = "Register Name is invalid", regexp = "[A-Za-z]{2,30}")
//        @NotBlank
//        String name,
//
//        @Pattern(message = "Register Email is invalid", regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
//        @Email
//        String email
//) {
//}