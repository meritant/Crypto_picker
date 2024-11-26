package com.example.cryptopicker.controller;

import org.springframework.web.bind.annotation.*;
import com.example.cryptopicker.service.CryptoPriceService;
import com.example.cryptopicker.model.dto.CryptoPriceDTO;
import java.util.List;

@RestController
@RequestMapping("/api/crypto")
public class CryptoPriceController {
    private final CryptoPriceService cryptoPriceService;

    public CryptoPriceController(CryptoPriceService cryptoPriceService) {
        this.cryptoPriceService = cryptoPriceService;
    }

    @GetMapping("/top-prices")
    public List<CryptoPriceDTO> getTopCryptoPrices(
        @RequestParam(defaultValue = "10") int limit
    ) {
        return cryptoPriceService.fetchTopCryptoPrices(limit);
    }

    @GetMapping("/price/{symbol}")
    public CryptoPriceDTO getCryptoPrice(@PathVariable String symbol) {
        return cryptoPriceService.fetchPriceBySymbol(symbol);
    }
}