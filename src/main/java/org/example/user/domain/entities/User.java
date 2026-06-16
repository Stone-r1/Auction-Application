package org.example.user.domain.entities;


import jakarta.persistence.*;
import lombok.Data;
import org.example.shared.data.Roles;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Data
@Entity
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_users_username", columnNames = "username"),
                @UniqueConstraint(name = "uk_users_email", columnNames = "email")
        }
)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private Boolean enabled = false;

    @Column(name = "last_modified")
    private LocalDateTime lastModified;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Roles role = Roles.USER;
}
