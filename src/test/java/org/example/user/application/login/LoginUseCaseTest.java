package org.example.user.application.login;

import org.example.user.domain.entities.User;
import org.example.shared.domain.TokenRepository;
import org.example.user.domain.services.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class LoginUseCaseTest {

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private TokenRepository tokenRepository;

    @InjectMocks
    private LoginUseCase loginUseCase;

    @Test
    void login_returnsGeneratedToken_whenCredentialsAreValid() {
        LoginRequest request = new LoginRequest("john", "rawPassword");
        User user = new User();

        when(
                authenticationService.authenticateUser("john", "rawPassword")
        ).thenReturn(user);

        when(
                tokenRepository.generateToken(user)
        ).thenReturn("jwt-token");

        String result = loginUseCase.login(request);

        assertThat(result).isEqualTo("jwt-token");
    }

    @Test
    void login_propagatesUsernameNotFoundException_whenUserDoesNotExist() {
        LoginRequest request = new LoginRequest("unknown", "anyPassword");

        when(
                authenticationService.authenticateUser("unknown", "anyPassword")
        ).thenThrow(UsernameNotFoundException.class);

        assertThatThrownBy(() -> loginUseCase.login(request))
                .isInstanceOf(UsernameNotFoundException.class);
    }

    @Test
    void login_propagatesAuthenticationCredentialsNotFoundException_whenPasswordIsWrong() {
        LoginRequest request = new LoginRequest("john", "wrongPassword");

        when(
                authenticationService.authenticateUser("john", "wrongPassword")
        ).thenThrow(AuthenticationCredentialsNotFoundException.class);

        assertThatThrownBy(() -> loginUseCase.login(request))
                .isInstanceOf(AuthenticationCredentialsNotFoundException.class);
    }

    @Test
    void login_propagatesDisabledException_whenAccountIsNotVerified() {
        LoginRequest request = new LoginRequest("john", "rawPassword");

        when(
                authenticationService.authenticateUser("john", "rawPassword")
        ).thenThrow(DisabledException.class);

        assertThatThrownBy(() -> loginUseCase.login(request))
                .isInstanceOf(DisabledException.class);
    }
}

