package org.example.user.application.register;

import jakarta.persistence.EntityExistsException;
import org.example.user.domain.entities.User;
import org.example.user.domain.services.AuthenticationService;
import org.example.user.domain.services.VerificationTokenService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class RegisterUseCaseTest {

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private VerificationTokenService verificationTokenService;

    @InjectMocks
    private RegisterUseCase registerUseCase;

    @Test
    void registerUser_returnsSuccessMessageContainingUsername_whenRegistrationSucceeds() {
        RegisterRequest request = new RegisterRequest("alice", "P@ssw0rd1", "alice@example.com", LocalDate.of(1990, 1, 1));

        User saved = new User();
        saved.setUsername("alice");

        when(
                authenticationService.registerUser(any())
        ).thenReturn(saved);

        String result = registerUseCase.registerUser(request);

        assertThat(result).contains("alice");
    }

    @Test
    void registerUser_buildsUserFromAllRequestFields() {
        LocalDate birthDate = LocalDate.of(1990, 5, 20);
        RegisterRequest request = new RegisterRequest("alice", "P@ssw0rd1", "alice@example.com", birthDate);
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        User saved = new User();
        saved.setUsername("alice");

        when(
                authenticationService.registerUser(userCaptor.capture())
        ).thenReturn(saved);

        registerUseCase.registerUser(request);

        User captured = userCaptor.getValue();
        assertThat(captured.getUsername()).isEqualTo("alice");
        assertThat(captured.getEmail()).isEqualTo("alice@example.com");
        assertThat(captured.getPassword()).isEqualTo("P@ssw0rd1");
        assertThat(captured.getBirthDate()).isEqualTo(birthDate);
    }

    @Test
    void registerUser_sendsVerificationEmailToRegisteredUser() {
        RegisterRequest request = new RegisterRequest("alice", "P@ssw0rd1", "alice@example.com", LocalDate.of(1990, 1, 1));

        User saved = new User();
        saved.setUsername("alice");

        when(
                authenticationService.registerUser(any())
        ).thenReturn(saved);

        registerUseCase.registerUser(request);

        verify(verificationTokenService).sendVerificationEmail(saved);
    }

    @Test
    void registerUser_propagatesEntityExistsException_whenUsernameIsAlreadyTaken() {
        RegisterRequest request = new RegisterRequest("alice", "P@ssw0rd1", "alice@example.com", null);

        when(
                authenticationService.registerUser(any())
        ).thenThrow(EntityExistsException.class);

        assertThatThrownBy(() -> registerUseCase.registerUser(request))
                .isInstanceOf(EntityExistsException.class);
    }

    @Test
    void registerUser_propagatesEntityExistsException_whenEmailIsAlreadyTaken() {
        RegisterRequest request = new RegisterRequest("newUser", "P@ssw0rd1", "taken@example.com", null);

        when(
                authenticationService.registerUser(any())
        ).thenThrow(EntityExistsException.class);

        assertThatThrownBy(() -> registerUseCase.registerUser(request))
                .isInstanceOf(EntityExistsException.class);
    }

    @Test
    void registerUser_doesNotSendVerificationEmail_whenRegistrationFails() {
        RegisterRequest request = new RegisterRequest("alice", "P@ssw0rd1", "alice@example.com", null);

        when(
                authenticationService.registerUser(any())
        ).thenThrow(EntityExistsException.class);

        assertThatThrownBy(() -> registerUseCase.registerUser(request))
                .isInstanceOf(EntityExistsException.class);

        verify(verificationTokenService, never()).sendVerificationEmail(any());
    }
}

