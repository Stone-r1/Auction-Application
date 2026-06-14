package org.example.user.infrastructure.persistance;

import org.example.user.domain.entities.User;
import org.example.user.domain.entities.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface JpaVerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    Optional<VerificationToken> findByToken(String token);
    void deleteByUser(User user);
}
