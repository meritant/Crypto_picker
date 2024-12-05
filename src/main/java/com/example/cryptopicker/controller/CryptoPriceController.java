package com.example.cryptopicker.controller;

import com.example.cryptopicker.service.CryptoPriceService;
import com.example.cryptopicker.model.dto.CryptoPriceDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/crypto")
@Slf4j
public class CryptoPriceController {
    private final CryptoPriceService cryptoPriceService;
    
    public CryptoPriceController(CryptoPriceService cryptoPriceService) {
        this.cryptoPriceService = cryptoPriceService;
    }

    @GetMapping("/top-three")
    public List<CryptoPriceDTO> getTopThree() {
        try {
            return cryptoPriceService.fetchTopCryptoPrices(3);
        } catch (Exception e) {
            log.error("Error fetching top three cryptos: ", e);
            throw e;
        }
    }

    @GetMapping("/top-prices")
    public List<CryptoPriceDTO> getTopCryptoPrices(
        @RequestParam(defaultValue = "10") int limit
    ) {
        try {
            return cryptoPriceService.fetchTopCryptoPrices(limit);
        } catch (Exception e) {
            log.error("Error fetching top cryptos: ", e);
            throw e;
        }
    }

    @GetMapping("/price/{symbol}")
    public CryptoPriceDTO getCryptoPrice(@PathVariable String symbol) {
        try {
            return cryptoPriceService.fetchPriceBySymbol(symbol);
        } catch (Exception e) {
            log.error("Error fetching price for symbol {}: ", symbol, e);
            throw e;
        }
    }
}