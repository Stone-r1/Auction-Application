package org.example.user.infrastructure.persistance;


import org.example.user.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface JpaAuthenticationRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserId(Long userId);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<String> findEmailByUserId(Long userId);
    User save(User user);
}
