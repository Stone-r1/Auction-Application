package org.example.user.presentation.controllers;

import org.example.user.application.login.LoginRequest;
import org.example.user.application.login.LoginUseCase;
import org.example.user.application.register.RegisterRequest;
import org.example.user.application.register.RegisterUseCase;
import org.example.user.application.verification.VerificationUseCase;
import org.example.user.domain.entities.User;
import org.example.user.domain.services.VerificationResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {

    @Mock
    private LoginUseCase loginUseCase;

    @Mock
    private RegisterUseCase registerUseCase;

    @Mock
    private VerificationUseCase verificationUseCase;

    @InjectMocks
    private AuthenticationController authenticationController;

    @Test
    void login_returnsTokenProducedByUseCase() {
        LoginRequest request = new LoginRequest("john", "rawPassword");

        when(
                loginUseCase.login(request)
        ).thenReturn("jwt-token");

        String result = authenticationController.login(request);

        assertThat(result).isEqualTo("jwt-token");
    }

    @Test
    void register_returnsSuccessMessageProducedByUseCase() {
        RegisterRequest request = new RegisterRequest("alice", "P@ssw0rd1", "alice@example.com", LocalDate.of(1990, 1, 1));

        when(
                registerUseCase.registerUser(request)
        ).thenReturn("User with username alice was created successfully! Check your email to verify your account.");

        String result = authenticationController.register(request);

        assertThat(result).isEqualTo("User with username alice was created successfully! Check your email to verify your account.");
    }

    @Test
    void verify_returnsSuccessWithMessage_whenTokenIsValid() {
        User user = new User();

        when(
                verificationUseCase.verify("valid-token")
        ).thenReturn(new VerificationResult.Success(user));

        ResponseEntity<String> response = authenticationController.verify("valid-token");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Email verified successfully. You can now log in.");
    }

    @Test
    void verify_returnsSuccessWithAlreadyVerifiedMessage_whenAccountIsAlreadyEnabled() {
        User user = new User();

        when(
                verificationUseCase.verify("used-token")
        ).thenReturn(new VerificationResult.AlreadyVerified(user));

        ResponseEntity<String> response = authenticationController.verify("used-token");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("This account is already verified.");
    }

    @Test
    void verify_returns410WithExpiredMessage_whenTokenHasExpired() {
        when(
                verificationUseCase.verify("expired-token")
        ).thenReturn(new VerificationResult.Expired());

        ResponseEntity<String> response = authenticationController.verify("expired-token");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.GONE);
        assertThat(response.getBody()).isEqualTo("This verification link has expired.");
    }

    @Test
    void verify_returns404WithInvalidMessage_whenTokenDoesNotExist() {
        when(
                verificationUseCase.verify("ghost-token")
        ).thenReturn(new VerificationResult.NotFound());

        ResponseEntity<String> response = authenticationController.verify("ghost-token");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isEqualTo("This verification link is invalid.");
    }
}

