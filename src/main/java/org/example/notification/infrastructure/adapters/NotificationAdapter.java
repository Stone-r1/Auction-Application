package org.example.notification.infrastructure.adapters;


import org.example.notification.domain.repositories.NotificationRepository;
import org.example.user.domain.entities.User;
import org.example.user.domain.repositories.AuthenticationRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public class NotificationAdapter implements NotificationRepository {
    private final AuthenticationRepository authenticationRepository;

    public NotificationAdapter(
            AuthenticationRepository authenticationRepository
    ) {
        this.authenticationRepository = authenticationRepository;
    }

    @Override
    public Optional<String> findEmailByUserId(Long userId) {
        return authenticationRepository.findByUserId(userId)
                .map(User::getEmail);
    }
}
