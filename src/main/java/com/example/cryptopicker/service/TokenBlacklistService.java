package com.example.cryptopicker.service;

import org.springframework.stereotype.Service;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class TokenBlacklistService {
    private final ConcurrentHashMap<String, Long> blacklistedTokens = new ConcurrentHashMap<>();
    private final ScheduledExecutorService cleanupExecutor = Executors.newSingleThreadScheduledExecutor();

    public TokenBlacklistService() {
        // Clean up expired tokens every hour
        cleanupExecutor.scheduleAtFixedRate(this::cleanupExpiredTokens, 1, 1, TimeUnit.HOURS);
    }

    public void blacklistToken(String token) {
        blacklistedTokens.put(token, System.currentTimeMillis());
    }

    public boolean isBlacklisted(String token) {
        return blacklistedTokens.containsKey(token);
    }

    private void cleanupExpiredTokens() {
        long now = System.currentTimeMillis();
        blacklistedTokens.entrySet().removeIf(entry -> 
            now - entry.getValue() > TimeUnit.DAYS.toMillis(1) // Remove tokens older than 1 day
        );
    }
}