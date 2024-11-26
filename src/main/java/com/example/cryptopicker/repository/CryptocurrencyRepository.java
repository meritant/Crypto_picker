package com.example.cryptopicker.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.example.cryptopicker.model.Cryptocurrency;
import java.util.List;
import java.util.Optional;

@Repository
public interface CryptocurrencyRepository extends JpaRepository<Cryptocurrency, String> {
    @Query("SELECT c FROM Cryptocurrency c ORDER BY c.marketCap DESC")
    List<Cryptocurrency> findAllOrderByMarketCapDesc(Pageable pageable);
    
    Optional<Cryptocurrency> findBySymbolIgnoreCase(String symbol);
}