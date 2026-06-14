package org.example.user.presentation.controllers;


import jakarta.validation.Valid;
import org.example.user.application.login.LoginRequest;
import org.example.user.application.login.LoginUseCase;
import org.example.user.application.register.RegisterRequest;
import org.example.user.application.register.RegisterUseCase;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class AuthenticationController {
    private final LoginUseCase loginUseCase;
    private final RegisterUseCase registerUseCase;

    public AuthenticationController(
            LoginUseCase loginUseCase,
            RegisterUseCase registerUseCase) {
        this.loginUseCase = loginUseCase;
        this.registerUseCase = registerUseCase;
    }

    @PostMapping("/login")
    public String login(
            @Valid @RequestBody LoginRequest loginRequest
    ) {
        return loginUseCase.login(loginRequest);
    }

    @PostMapping("/register")
    public String register(
            @Valid @RequestBody RegisterRequest registerRequest
    ) {
        return registerUseCase.registerUser(registerRequest);
    }
}
