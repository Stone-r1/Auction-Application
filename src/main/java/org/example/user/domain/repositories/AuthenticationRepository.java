package org.example.user.domain.repositories;


import org.example.user.domain.entities.User;

import java.util.Optional;

public interface AuthenticationRepository {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
}
