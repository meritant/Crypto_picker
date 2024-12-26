package com.example.cryptopicker.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import com.example.cryptopicker.model.dto.CryptoPriceDTO;
import com.example.cryptopicker.model.Cryptocurrency;
import com.example.cryptopicker.repository.CryptocurrencyRepository;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.time.LocalDateTime;
import java.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@Slf4j
public class CryptoPriceService {
    private static final Logger logger = LoggerFactory.getLogger(CryptoPriceService.class);
    private static final Duration CACHE_DURATION = Duration.ofMinutes(5);
    
    private final WebClient webClient;
    private final CryptocurrencyRepository cryptocurrencyRepository;
    private final String apiKey;
    
    public CryptoPriceService(CryptocurrencyRepository cryptocurrencyRepository, 
            @Value("${coingecko.api.key}") String apiKey) {
		this.cryptocurrencyRepository = cryptocurrencyRepository;
		this.apiKey = apiKey;
		this.webClient = WebClient.builder()
		.baseUrl("https://api.coingecko.com/api/v3")
		.defaultHeader("x-cg-demo-api-key", apiKey)  
		.build();
		}
    
    
    public List<CryptoPriceDTO> fetchTopCryptoPrices(int limit) {
    	   List<Cryptocurrency> cachedData = cryptocurrencyRepository.findAllOrderByMarketCapDesc(PageRequest.of(0, limit));

    	   if (!cachedData.isEmpty() && isDataFresh(cachedData.get(0).getLastUpdated())) {
    	       log.info("Returning cached data for top {} cryptocurrencies", limit);
    	       return cachedData.stream()
    	           .map(this::convertToDTO)
    	           .collect(Collectors.toList());
    	   }

    	   try {
    	       log.info("Fetching fresh data from CoinGecko API for top {} cryptocurrencies", limit);
    	       
    	       List<CryptoPriceDTO> allPrices = new ArrayList<>();
    	       
    	       if (limit > 300) limit = 300; // Cap at 300

    	       int pages = (limit + 99) / 100;
    	       
    	       for (int i = 1; i <= pages; i++) {
    	           final int currentPage = i;
    	           final int perPage = Math.min(100, limit - (currentPage - 1) * 100);
    	           
    	           List<CryptoPriceDTO> pagePrices = webClient.get()
    	               .uri(uriBuilder -> uriBuilder
    	                   .path("/coins/markets")
    	                   .queryParam("vs_currency", "usd")
    	                   .queryParam("order", "market_cap_desc")
    	                   .queryParam("per_page", perPage)
    	                   .queryParam("page", currentPage)
    	                   .queryParam("sparkline", false)
    	                   .build())
    	               .retrieve()
    	               .bodyToFlux(CryptoPriceDTO.class)
    	               .collectList()
    	               .block();

    	           if (pagePrices != null && !pagePrices.isEmpty()) {
    	               allPrices.addAll(pagePrices);
    	           }
    	       }

    	       if (!allPrices.isEmpty()) {
    	           saveToDatabase(allPrices);
    	       }

    	       return allPrices;
    	   } catch (WebClientResponseException e) {
    	       log.error("API error: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
    	       
    	       if (!cachedData.isEmpty()) {
    	           log.info("API call failed, returning cached data");
    	           return cachedData.stream()
    	               .map(this::convertToDTO)
    	               .collect(Collectors.toList());
    	       }
    	       
    	       throw new RuntimeException("Failed to fetch crypto prices: " + e.getMessage());
    	   }
    	}
    
    
    
    
    
    // ... rest of the service methods remain the same ...

    private boolean isDataFresh(LocalDateTime lastUpdated) {
        return lastUpdated != null && 
               Duration.between(lastUpdated, LocalDateTime.now()).compareTo(CACHE_DURATION) < 0;
    }
    
    
    
    
    
//    private CryptoPriceDTO convertToDTO(Cryptocurrency entity) {
//        CryptoPriceDTO dto = new CryptoPriceDTO();
//        dto.setId(entity.getId());
//        dto.setSymbol(entity.getSymbol());
//        dto.setName(entity.getName());
//        dto.setCurrentPrice(entity.getCurrentPrice());
//        dto.setMarketCap(entity.getMarketCap());
//        return dto;
//    }
    
    
    private CryptoPriceDTO convertToDTO(Cryptocurrency entity) {
        CryptoPriceDTO dto = new CryptoPriceDTO();
        dto.setId(entity.getId());
        dto.setSymbol(entity.getSymbol());
        dto.setName(entity.getName());
        dto.setCurrentPrice(entity.getCurrentPrice());
        dto.setMarketCap(entity.getMarketCap());
        dto.setPriceChangePercentage24h(entity.getPriceChangePercentage24h());
        return dto;
    }
    
    
    
    

    
    private void saveToDatabase(List<CryptoPriceDTO> prices) {
        try {
            List<Cryptocurrency> cryptoEntities = prices.stream()
                .map(this::convertToEntity)
                .collect(Collectors.toList());
            
            log.info("Saving {} cryptocurrencies to database", cryptoEntities.size());
            cryptocurrencyRepository.saveAll(cryptoEntities);
            log.info("Successfully saved cryptocurrencies");
        } catch (Exception e) {
            log.error("Error saving to database: ", e);
            throw new RuntimeException("Failed to save cryptocurrencies", e);
        }
    }
    
    
//    private Cryptocurrency convertToEntity(CryptoPriceDTO dto) {
//        Cryptocurrency crypto = new Cryptocurrency();
//        crypto.setId(dto.getId());
//        crypto.setSymbol(dto.getSymbol());
//        crypto.setName(dto.getName());
//        crypto.setCurrentPrice(dto.getCurrentPrice());
//        crypto.setMarketCap(dto.getMarketCap());
//        crypto.setLastUpdated(LocalDateTime.now());
//        return crypto;
//    }
    
    
    
    private Cryptocurrency convertToEntity(CryptoPriceDTO dto) {
        Cryptocurrency crypto = new Cryptocurrency();
        crypto.setId(dto.getId());
        crypto.setSymbol(dto.getSymbol());
        crypto.setName(dto.getName());
        crypto.setCurrentPrice(dto.getCurrentPrice());
        crypto.setMarketCap(dto.getMarketCap());
        crypto.setPriceChangePercentage24h(dto.getPriceChangePercentage24h());
        crypto.setLastUpdated(LocalDateTime.now());
        return crypto;
    }
    
    
    

    public CryptoPriceDTO fetchPriceBySymbol(String symbol) {
        logger.info("Fetching price for symbol: {}", symbol);

        // First check the database for cached data
        Optional<Cryptocurrency> cachedCrypto = cryptocurrencyRepository.findBySymbolIgnoreCase(symbol);
        
        // If we have recent cached data, use it
        if (cachedCrypto.isPresent() && isDataFresh(cachedCrypto.get().getLastUpdated())) {
            logger.info("Returning cached data for symbol: {}", symbol);
            return convertToDTO(cachedCrypto.get());
        }

        // If no fresh cache, fetch from API
        try {
            logger.info("Fetching fresh data from CoinGecko API for symbol: {}", symbol);
            List<CryptoPriceDTO> prices = webClient.get()
                .uri(uriBuilder -> uriBuilder
                    .path("/coins/markets")
                    .queryParam("vs_currency", "usd")
                    .queryParam("ids", symbol.toLowerCase())
                    .queryParam("order", "market_cap_desc")
                    .queryParam("per_page", 1)
                    .queryParam("page", 1)
                    .queryParam("sparkline", false)
                    .build())
                .retrieve()
                .bodyToFlux(CryptoPriceDTO.class)
                .collectList()
                .block();

            if (prices != null && !prices.isEmpty()) {
                CryptoPriceDTO price = prices.get(0);
                saveToDatabase(List.of(price));
                return price;
            }

            // If API returns no data but we have cached data (even if old), use it
            if (cachedCrypto.isPresent()) {
                logger.info("No API data available, returning cached data for symbol: {}", symbol);
                return convertToDTO(cachedCrypto.get());
            }

            logger.error("No data found for symbol: {}", symbol);
            throw new RuntimeException("Cryptocurrency not found: " + symbol);

        } catch (WebClientResponseException e) {
            logger.error("API error for symbol {}: {} - {}", 
                symbol, e.getStatusCode(), e.getResponseBodyAsString());
            
            // If API call fails but we have cached data (even if old), use it
            if (cachedCrypto.isPresent()) {
                logger.info("API call failed, returning cached data for symbol: {}", symbol);
                return convertToDTO(cachedCrypto.get());
            }
            
            throw new RuntimeException("Failed to fetch price for " + symbol + ": " + e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error fetching price for symbol: " + symbol, e);
            
            // If any other error occurs but we have cached data, use it
            if (cachedCrypto.isPresent()) {
                logger.info("Error occurred, returning cached data for symbol: {}", symbol);
                return convertToDTO(cachedCrypto.get());
            }
            
            throw new RuntimeException("Failed to fetch price for " + symbol, e);
        }
    }
}