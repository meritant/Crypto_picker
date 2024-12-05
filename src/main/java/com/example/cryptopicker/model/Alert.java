package com.example.cryptopicker.model;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "alerts")
public class Alert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String cryptoSymbol;
    private BigDecimal targetPrice;
    
    @Enumerated(EnumType.STRING)
    private AlertType type;
    
    @Enumerated(EnumType.STRING)
    private AlertStatus status;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdated;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        lastUpdated = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        lastUpdated = LocalDateTime.now();
    }
}