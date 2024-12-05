package com.example.cryptopicker.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import com.example.cryptopicker.model.AlertType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAlertRequest {
    @NotBlank
    private String cryptoSymbol;
    
    @NotNull
    private BigDecimal targetPrice;
    
    @NotNull
    private AlertType type;
}