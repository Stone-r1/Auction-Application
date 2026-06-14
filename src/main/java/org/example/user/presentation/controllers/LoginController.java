package org.example.user.presentation.controllers;


import jakarta.validation.Valid;
import org.example.user.application.login.LoginRequest;
import org.example.user.application.login.LoginUseCase;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/login")
public class LoginController {
    private final LoginUseCase loginUseCase;

    public LoginController(
            LoginUseCase loginUseCase
    ) {
        this.loginUseCase = loginUseCase;
    }

    @PostMapping()
    public String login(
            @Valid @RequestBody LoginRequest loginRequest
    ) {
        return loginUseCase.login(loginRequest);
    }
}
