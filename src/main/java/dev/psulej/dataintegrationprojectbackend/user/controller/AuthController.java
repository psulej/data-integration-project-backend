package dev.psulej.dataintegrationprojectbackend.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//@RequiredArgsConstructor
//@RestController
//@RequestMapping("/auth")
//public class AuthController {
//
//    private final UserService userService;
//
//    @PostMapping("/login")
//    public LoginResponse login(@Valid @RequestBody LoginRequest loginRequest) {
//        String token = userService.authenticateAndGetToken(loginRequest.login(), loginRequest.password());
//        return new LoginResponse(token);
//    }
//
//    @ResponseStatus(HttpStatus.CREATED)
//    @PostMapping("/register")
//    public void register(@Valid @RequestBody RegisterRequest registerRequest) {
//        userService.register(registerRequest);
//    }
//}