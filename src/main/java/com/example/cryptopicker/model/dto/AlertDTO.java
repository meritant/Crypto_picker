package com.example.cryptopicker.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import com.example.cryptopicker.model.AlertType;
import com.example.cryptopicker.model.AlertStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlertDTO {
    private Long id;
    private String cryptoSymbol;
    private BigDecimal targetPrice;
    private AlertType type;
    private AlertStatus status;
    private String userEmail;
}