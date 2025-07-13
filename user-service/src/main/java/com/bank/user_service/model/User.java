package com.bank.user_service.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incremented primary key
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 10)
    private String mobile;

//    @Column(nullable = false)
//    private String role = "USER"; // default value

    @Column(nullable = false)
    private String role; // Should be ROLE_USER or ROLE_ADMIN

    private boolean active = true; // can be used for blocking the account
}

//We’ll hash the password before saving (in service layer).
