package com.example.cryptopicker.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class CryptoPriceDTO {
    @JsonProperty("id")
    private String id;

    @JsonProperty("symbol")
    private String symbol;

    @JsonProperty("name")
    private String name;

    @JsonProperty("current_price")
    private BigDecimal currentPrice;

    @JsonProperty("market_cap")
    private BigDecimal marketCap;

    // Add no-args constructor
    public CryptoPriceDTO() {}

    // Optional: Add all-args constructor
    public CryptoPriceDTO(String id, String symbol, String name, 
                           BigDecimal currentPrice, BigDecimal marketCap) {
        this.id = id;
        this.symbol = symbol;
        this.name = name;
        this.currentPrice = currentPrice;
        this.marketCap = marketCap;
    }
}