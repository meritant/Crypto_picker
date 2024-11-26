package com.example.cryptopicker.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import com.example.cryptopicker.model.dto.CryptoPriceDTO;
import reactor.core.publisher.Flux;
import java.util.List;

@Service
public class CryptoPriceService {
    private final WebClient webClient;

    public CryptoPriceService() {
        this.webClient = WebClient.builder()
            .baseUrl("https://api.coingecko.com/api/v3")
            .build();
    }

    public List<CryptoPriceDTO> fetchTopCryptoPrices(int limit) {
        return webClient.get()
            .uri("/coins/markets?vs_currency=usd&order=market_cap_desc&per_page=" + limit + "&page=1&sparkline=false")
            .retrieve()
            .bodyToFlux(CryptoPriceDTO.class)
            .collectList()
            .block();
    }

    public CryptoPriceDTO fetchPriceBySymbol(String symbol) {
        return webClient.get()
            .uri("/coins/" + symbol.toLowerCase())
            .retrieve()
            .bodyToMono(CryptoPriceDTO.class)
            .block();
    }
}