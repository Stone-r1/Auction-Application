package org.example.user.domain.entities;


import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;


@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    private String username;

    private String password;

    private String email;

    @Column(name = "birth_date")
    private Date birthDate;

    @Column(name = "last_modified")
    private Date lastModified; // can't change password too frequently - warning
}
