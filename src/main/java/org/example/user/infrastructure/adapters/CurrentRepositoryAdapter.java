package org.example.user.infrastructure.adapters;


import org.example.shared.infrastructure.security.CustomUserDetails;
import org.example.user.domain.entities.User;
import org.example.user.domain.repositories.CurrentUserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Objects;


@Component
public class CurrentRepositoryAdapter implements CurrentUserRepository {

    @Override
    public User getCurrentUser() {
        Object principal = Objects.requireNonNull(
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                ).getPrincipal();

        if (principal instanceof CustomUserDetails userDetails) {
            return userDetails.getUser();
        }

        throw new IllegalStateException(
                "No authenticated user in security context"
        );
    }
}
