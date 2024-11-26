package com.example.cryptopicker.model;

import lombok.Data;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "cryptocurrencies")
public class Cryptocurrency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true)
    private String symbol;
    
    private String name;
    private BigDecimal currentPrice;
    private BigDecimal marketCap;
    private LocalDateTime lastUpdated;
}