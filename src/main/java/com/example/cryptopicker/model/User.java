package com.example.cryptopicker.model;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    private String password;
    private String name;
    private String role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Alert> alerts = new ArrayList<>();

    private LocalDateTime createdAt;
    private LocalDateTime lastLogin;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}