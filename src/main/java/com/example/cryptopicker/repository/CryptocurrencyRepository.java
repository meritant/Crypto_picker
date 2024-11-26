package com.example.cryptopicker.repository;

import com.example.cryptopicker.model.Cryptocurrency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CryptocurrencyRepository extends JpaRepository<Cryptocurrency, Long> {
    Optional<Cryptocurrency> findBySymbol(String symbol);
    
    // Custom query methods can be added here
    List<Cryptocurrency> findTop10ByOrderByMarketCapDesc();
}