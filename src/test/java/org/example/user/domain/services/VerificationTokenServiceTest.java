package org.example.user.domain.services;

import org.example.user.domain.entities.User;
import org.example.user.domain.entities.VerificationToken;
import org.example.user.domain.repositories.AuthenticationRepository;
import org.example.user.domain.repositories.EmailSenderRepository;
import org.example.user.domain.repositories.VerificationTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class VerificationTokenServiceTest {

    @Mock
    private VerificationTokenRepository verificationTokenRepository;

    @Mock
    private AuthenticationRepository authenticationRepository;

    @Mock
    private EmailSenderRepository emailSenderRepository;

    private VerificationTokenService verificationTokenService;

    private User unverifiedUser;

    @BeforeEach
    void setUp() {
        verificationTokenService = new VerificationTokenService(
                verificationTokenRepository,
                authenticationRepository,
                emailSenderRepository,
                "http://localhost:8080",
                Duration.ofHours(24)
        );

        unverifiedUser = new User();
        unverifiedUser.setUsername("alice");
        unverifiedUser.setEmail("alice@example.com");
        unverifiedUser.setEnabled(false);
    }

    @Test
    void sendVerificationEmail_deletesExistingTokenBeforeSendingNew() {
        when(
                verificationTokenRepository.save(any())
        ).thenAnswer(inv -> inv.getArgument(0));

        verificationTokenService.sendVerificationEmail(unverifiedUser);

        verify(verificationTokenRepository).deleteByUser(unverifiedUser);
    }

    @Test
    void sendVerificationEmail_savesTokenWithFutureExpiryAndUserAttached() {
        ArgumentCaptor<VerificationToken> tokenCaptor = ArgumentCaptor.forClass(VerificationToken.class);

        when(
                verificationTokenRepository.save(any())
        ).thenAnswer(inv -> inv.getArgument(0));

        verificationTokenService.sendVerificationEmail(unverifiedUser);

        verify(verificationTokenRepository).save(tokenCaptor.capture());
        VerificationToken saved = tokenCaptor.getValue();
        assertThat(saved.getUser()).isEqualTo(unverifiedUser);
        assertThat(saved.getToken()).isNotBlank();
        assertThat(saved.getExpiresAt()).isAfter(Instant.now());
    }

    @Test
    void sendVerificationEmail_sendsEmailToUserAddressWithVerificationLink() {
        ArgumentCaptor<VerificationToken> tokenCaptor = ArgumentCaptor.forClass(VerificationToken.class);

        when(
                verificationTokenRepository.save(any())
        ).thenAnswer(inv -> inv.getArgument(0));

        verificationTokenService.sendVerificationEmail(unverifiedUser);

        verify(verificationTokenRepository).save(tokenCaptor.capture());
        String expectedTokenValue = tokenCaptor.getValue().getToken();

        verify(emailSenderRepository).send(
                eq("alice@example.com"),
                eq("Verify Your Account"),
                contains("http://localhost:8080/verify?token=" + expectedTokenValue)
        );
    }

    @Test
    void sendVerificationEmail_emailBodyContainsTokenExpiryDateInHours() {
        when(
                verificationTokenRepository.save(any())
        ).thenAnswer(inv -> inv.getArgument(0));

        verificationTokenService.sendVerificationEmail(unverifiedUser);

        verify(emailSenderRepository).send(any(), any(), contains("24"));
    }

    @Test
    void verify_returnsSuccess_andEnablesUser_whenTokenIsValidAndUserIsNotYetVerified() {
        VerificationToken token = validToken(unverifiedUser);

        when(
                verificationTokenRepository.findByToken("valid-token")
        ).thenReturn(Optional.of(token));

        when(
                authenticationRepository.save(any())
        ).thenAnswer(inv -> inv.getArgument(0));

        VerificationResult result = verificationTokenService.verify("valid-token");

        assertThat(result).isInstanceOf(VerificationResult.Success.class);
        assertThat(((VerificationResult.Success) result).user().getEnabled()).isTrue();
    }

    @Test
    void verify_savesEnabledUser_andDeletesToken_whenVerificationSucceeds() {
        VerificationToken token = validToken(unverifiedUser);

        when(
                verificationTokenRepository.findByToken("valid-token")
        ).thenReturn(Optional.of(token));

        when(
                authenticationRepository.save(any())
        ).thenAnswer(inv -> inv.getArgument(0));

        verificationTokenService.verify("valid-token");

        verify(authenticationRepository).save(unverifiedUser);
        verify(verificationTokenRepository).delete(token);
    }

    @Test
    void verify_returnsNotFound_whenTokenDoesNotExist() {
        when(
                verificationTokenRepository.findByToken("ghost-token")
        ).thenReturn(Optional.empty());

        VerificationResult result = verificationTokenService.verify("ghost-token");

        assertThat(result).isInstanceOf(VerificationResult.NotFound.class);
    }

    @Test
    void verify_returnsAlreadyVerified_whenUserAccountIsAlreadyEnabled() {
        unverifiedUser.setEnabled(true);
        VerificationToken token = validToken(unverifiedUser);

        when(
                verificationTokenRepository.findByToken("valid-token")
        ).thenReturn(Optional.of(token));

        VerificationResult result = verificationTokenService.verify("valid-token");

        assertThat(result).isInstanceOf(VerificationResult.AlreadyVerified.class);
        assertThat(((VerificationResult.AlreadyVerified) result).user()).isEqualTo(unverifiedUser);
    }

    @Test
    void verify_returnsExpired_whenTokenHasPassedItsExpiryTime() {
        VerificationToken expiredToken = new VerificationToken();
        expiredToken.setToken("expired-token");
        expiredToken.setUser(unverifiedUser);
        expiredToken.setExpiresAt(Instant.now().minus(Duration.ofMinutes(1)));

        when(
                verificationTokenRepository.findByToken("expired-token")
        ).thenReturn(Optional.of(expiredToken));

        VerificationResult result = verificationTokenService.verify("expired-token");

        assertThat(result).isInstanceOf(VerificationResult.Expired.class);
    }

    @Test
    void verify_doesNotSaveUserOrDeleteToken_whenTokenIsExpired() {
        VerificationToken expiredToken = new VerificationToken();
        expiredToken.setToken("expired-token");
        expiredToken.setUser(unverifiedUser);
        expiredToken.setExpiresAt(Instant.now().minus(Duration.ofMinutes(1)));

        when(
                verificationTokenRepository.findByToken("expired-token")
        ).thenReturn(Optional.of(expiredToken));

        verificationTokenService.verify("expired-token");

        verify(authenticationRepository, never()).save(any());
        verify(verificationTokenRepository, never()).delete(any());
    }

    @Test
    void verify_doesNotSaveUserOrDeleteToken_whenUserIsAlreadyVerified() {
        unverifiedUser.setEnabled(true);
        VerificationToken token = validToken(unverifiedUser);

        when(
                verificationTokenRepository.findByToken("valid-token")
        ).thenReturn(Optional.of(token));

        verificationTokenService.verify("valid-token");

        verify(authenticationRepository, never()).save(any());
        verify(verificationTokenRepository, never()).delete(any());
    }

    private VerificationToken validToken(
            User user
    ) {
        VerificationToken token = new VerificationToken();
        token.setToken("valid-token");
        token.setUser(user);
        token.setExpiresAt(Instant.now().plus(Duration.ofHours(24)));
        return token;
    }
}

