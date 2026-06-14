package org.example.user.presentation.controllers;


import jakarta.validation.Valid;
import org.example.user.application.login.LoginRequest;
import org.example.user.application.login.LoginUseCase;
import org.example.user.application.register.RegisterRequest;
import org.example.user.application.register.RegisterUseCase;
import org.example.user.application.verification.VerificationUseCase;
import org.example.user.domain.services.VerificationResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class AuthenticationController {
    private final LoginUseCase loginUseCase;
    private final RegisterUseCase registerUseCase;
    private final VerificationUseCase verificationUseCase;

    public AuthenticationController(
            LoginUseCase loginUseCase,
            RegisterUseCase registerUseCase,
            VerificationUseCase verificationUseCase
    ) {
        this.loginUseCase = loginUseCase;
        this.registerUseCase = registerUseCase;
        this.verificationUseCase = verificationUseCase;
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

    @GetMapping("/verify")
    public ResponseEntity<String> verify(
            @RequestParam String token
    ) {
        VerificationResult result = verificationUseCase.verify(token);

        if (result instanceof VerificationResult.Success) {
            return ResponseEntity.ok("Email verified successfully. You can now log in.");
        } else if (result instanceof VerificationResult.AlreadyVerified) {
            return ResponseEntity.ok("This account is already verified.");
        } else if (result instanceof VerificationResult.Expired) {
            return ResponseEntity.status(HttpStatus.GONE)
                    .body("This verification link has expired.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("This verification link is invalid.");
        }
    }
}
