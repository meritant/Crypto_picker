package com.example.cryptopicker.model;

import java.time.LocalDateTime;
import java.math.BigDecimal;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "cryptocurrencies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cryptocurrency {
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private String id;  // This comes from CoinGecko API
    
    @Column(nullable = false, length = 10)
    private String symbol;
    
    @Column(nullable = false, length = 100)
    private String name;
    
    @Column(name = "current_price", nullable = false, precision = 19, scale = 8)
    private BigDecimal currentPrice;
    
    @Column(name = "market_cap", precision = 19, scale = 2)
    private BigDecimal marketCap;
    
    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;
}