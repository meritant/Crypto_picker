package com.example.cryptopicker.model;

import lombok.Data;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "user_alerts")
public class UserAlert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    private User user;
    
    private String cryptoSymbol;
    private BigDecimal triggerPrice;
    private String alertType; // ABOVE, BELOW
}
