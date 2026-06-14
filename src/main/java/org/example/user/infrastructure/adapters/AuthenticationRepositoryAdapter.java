package org.example.user.infrastructure.adapters;


import org.example.user.domain.entities.User;
import org.example.user.domain.repositories.AuthenticationRepository;
import org.example.user.infrastructure.persistance.JpaAuthenticationRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public class AuthenticationRepositoryAdapter implements AuthenticationRepository {
    private final JpaAuthenticationRepository jpaAuthenticationRepository;

    public AuthenticationRepositoryAdapter(
            JpaAuthenticationRepository jpaAuthenticationRepository
    ) {
        this.jpaAuthenticationRepository = jpaAuthenticationRepository;
    }

    @Override
    public Optional<User> findByUsername(
            String username
    ) {
        return jpaAuthenticationRepository.findByUsername(username);
    }

    @Override
    public Optional<User> findByEmail(
            String email
    ) {
        return jpaAuthenticationRepository.findByEmail(email);
    }
}
