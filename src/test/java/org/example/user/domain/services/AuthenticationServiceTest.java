package org.example.user.domain.services;

import jakarta.persistence.EntityExistsException;
import org.example.user.domain.entities.User;
import org.example.user.domain.repositories.AuthenticationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private AuthenticationRepository authenticationRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthenticationService authenticationService;

    private User enabledUser;

    @BeforeEach
    void setUp() {
        enabledUser = new User();
        enabledUser.setUsername("john");
        enabledUser.setEmail("john@example.com");
        enabledUser.setPassword("encodedPassword");
        enabledUser.setEnabled(true);
    }

    @Test
    void getUserByUsername_returnsUser_whenCredentialsAreValidAndAccountIsEnabled() {
        when(
                authenticationRepository.findByUsername("john")
        ).thenReturn(Optional.of(enabledUser));

        when(
                passwordEncoder.matches("rawPassword", "encodedPassword")
        ).thenReturn(true);

        User result = authenticationService.getUserByUsername("john", "rawPassword");

        assertThat(result).isEqualTo(enabledUser);
    }

    @Test
    void getUserByUsername_throwsUsernameNotFoundException_whenUserDoesNotExist() {
        when(
                authenticationRepository.findByUsername("unknown")
        ).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authenticationService.getUserByUsername("unknown", "anyPassword"))
                .isInstanceOf(UsernameNotFoundException.class);
    }

    @Test
    void getUserByUsername_throwsAuthenticationCredentialsNotFoundException_whenPasswordIsWrong() {
        when(
                authenticationRepository.findByUsername("john")
        ).thenReturn(Optional.of(enabledUser));

        when(
                passwordEncoder.matches("wrongPassword", "encodedPassword")
        ).thenReturn(false);

        assertThatThrownBy(() -> authenticationService.getUserByUsername("john", "wrongPassword"))
                .isInstanceOf(AuthenticationCredentialsNotFoundException.class);
    }

    @Test
    void getUserByUsername_throwsDisabledException_whenAccountIsNotVerified() {
        enabledUser.setEnabled(false);

        when(
                authenticationRepository.findByUsername("john")
        ).thenReturn(Optional.of(enabledUser));

        when(
                passwordEncoder.matches("rawPassword", "encodedPassword")
        ).thenReturn(true);

        assertThatThrownBy(() -> authenticationService.getUserByUsername("john", "rawPassword"))
                .isInstanceOf(DisabledException.class);
    }

    @Test
    void registerUser_savesUserWithEncodedPassword_whenUsernameAndEmailAreUnique() {
        User newUser = new User();
        newUser.setUsername("alice");
        newUser.setEmail("alice@example.com");
        newUser.setPassword("rawPassword");

        when(
                authenticationRepository.findByUsername("alice")
        ).thenReturn(Optional.empty());

        when(
                authenticationRepository.findByEmail("alice@example.com")
        ).thenReturn(Optional.empty());

        when(
                passwordEncoder.encode("rawPassword")
        ).thenReturn("encodedPassword");

        when(
                authenticationRepository.save(any(User.class))
        ).thenAnswer(inv -> inv.getArgument(0));

        User result = authenticationService.registerUser(newUser);

        assertThat(result.getPassword()).isEqualTo("encodedPassword");
        verify(authenticationRepository).save(newUser);
    }

    @Test
    void registerUser_throwsEntityExistsException_whenUsernameIsAlreadyTaken() {
        User newUser = new User();
        newUser.setUsername("john");
        newUser.setEmail("other@example.com");
        newUser.setPassword("rawPassword");

        when(
                authenticationRepository.findByUsername("john")
        ).thenReturn(Optional.of(enabledUser));

        assertThatThrownBy(() -> authenticationService.registerUser(newUser))
                .isInstanceOf(EntityExistsException.class);

        verify(authenticationRepository, never()).save(any());
    }

    @Test
    void registerUser_throwsEntityExistsException_whenEmailIsAlreadyTaken() {
        User newUser = new User();
        newUser.setUsername("newUser");
        newUser.setEmail("john@example.com");
        newUser.setPassword("rawPassword");

        when(
                authenticationRepository.findByUsername("newUser")
        ).thenReturn(Optional.empty());

        when(
                authenticationRepository.findByEmail("john@example.com")
        ).thenReturn(Optional.of(enabledUser));

        assertThatThrownBy(() -> authenticationService.registerUser(newUser))
                .isInstanceOf(EntityExistsException.class);

        verify(authenticationRepository, never()).save(any());
    }
}

