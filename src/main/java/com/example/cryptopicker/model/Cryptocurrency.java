package com.example.cryptopicker.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cryptocurrencies")
public class Cryptocurrency {
    @Id
    private String id;
    private String symbol;
    private String name;
    private BigDecimal currentPrice;
    private BigDecimal marketCap;
    private LocalDateTime lastUpdated;
    @Column(name = "price_change_percentage_24h")
    private Double priceChangePercentage24h;
    
}