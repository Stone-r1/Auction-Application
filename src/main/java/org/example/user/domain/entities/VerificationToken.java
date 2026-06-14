package org.example.user.domain.entities;


import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;


@Data
@Entity
@Table(
        name = "verification_tokens",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_verification_tokens_token", columnNames = "token"),
                @UniqueConstraint(name = "uk_verification_tokens_user_id", columnNames = "user_id")
        }
)
public class VerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "verification_token_id")
    private Long verificationTokenId;

    @Column(nullable = false)
    private String token;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    public boolean isExpired() {
        return Instant.now().isAfter(expiresAt);
    }
}
