package org.example.user.domain.repositories;


import org.example.user.domain.entities.User;


public interface CurrentUserRepository {
    User getCurrentUser();
}
