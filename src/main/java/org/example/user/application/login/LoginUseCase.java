package org.example.user.application.login;

import org.example.user.domain.entities.User;
import org.example.user.domain.repositories.TokenRepository;
import org.example.user.domain.services.AuthenticationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class LoginUseCase {
    private final AuthenticationService authenticationService;
    private final TokenRepository tokenRepository;

    public LoginUseCase(
            AuthenticationService authenticationService,
            TokenRepository tokenRepository
    ) {
        this.authenticationService = authenticationService;
        this.tokenRepository = tokenRepository;
    }

    @Transactional(readOnly = true)
    public String login(
            LoginRequest loginRequest
    ) {
        User user = authenticationService.authenticateUser(
                loginRequest.username(),
                loginRequest.password()
        );

        return tokenRepository.generateToken(user);
    }
}
