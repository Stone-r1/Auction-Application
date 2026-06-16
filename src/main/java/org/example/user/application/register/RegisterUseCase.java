package org.example.user.application.register;


import org.example.user.domain.entities.User;
import org.example.user.domain.services.AuthenticationService;
import org.example.user.domain.services.VerificationTokenService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Service
public class RegisterUseCase {
    private final AuthenticationService authenticationService;
    private final VerificationTokenService verificationTokenService;

    public RegisterUseCase(
            AuthenticationService authenticationService,
            VerificationTokenService verificationTokenService
    ) {
        this.authenticationService = authenticationService;
        this.verificationTokenService = verificationTokenService;
    }

    @Transactional
    public String registerUser(
            RegisterRequest registerRequest
    ) {
        User user = new User();
        user.setUsername(registerRequest.username());
        user.setEmail(registerRequest.email());
        user.setPassword(registerRequest.password());
        user.setBirthDate(registerRequest.birthDate());
        user.setCreatedAt(LocalDateTime.now());
        user.setLastModified(LocalDateTime.now());

        User registeredUser = authenticationService.registerUser(user);
        verificationTokenService.sendVerificationEmail(registeredUser);

        return "User with username " + registeredUser.getUsername()
                + " was created successfully! Check your email to verify your account.";
    }
}
