package com.example.cryptopicker.service;

import com.example.cryptopicker.model.User;
import com.example.cryptopicker.model.UserCryptoSelection;
import com.example.cryptopicker.model.dto.CryptoPriceDTO;
import com.example.cryptopicker.repository.UserCryptoSelectionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;
//import java.time.LocalDateTime;

@Service
@Transactional
public class UserCryptoService {
    private final UserCryptoSelectionRepository userCryptoSelectionRepository;
    private final AuthService authService;
    private final CryptoPriceService cryptoPriceService;

    public UserCryptoService(UserCryptoSelectionRepository userCryptoSelectionRepository,
                           AuthService authService,
                           CryptoPriceService cryptoPriceService) {
        this.userCryptoSelectionRepository = userCryptoSelectionRepository;
        this.authService = authService;
        this.cryptoPriceService = cryptoPriceService;
    }

    public List<CryptoPriceDTO> getAvailableCryptos() {
        return cryptoPriceService.fetchTopCryptoPrices(300);
    }

    public List<CryptoPriceDTO> getUserSelection() {
        User currentUser = authService.getCurrentUser();
        UserCryptoSelection selection = userCryptoSelectionRepository.findByUser(currentUser)
            .orElse(new UserCryptoSelection());
        
        if (selection.getCryptoIds() == null || selection.getCryptoIds().isEmpty()) {
            return cryptoPriceService.fetchTopCryptoPrices(3); // Default to top 3
        }

        return selection.getCryptoIds().stream()
            .map(cryptoPriceService::fetchPriceBySymbol)
            .collect(Collectors.toList());
    }

    public void saveUserSelection(List<String> cryptoIds) {
        if (cryptoIds.size() > 15) {
            throw new IllegalArgumentException("Cannot select more than 15 cryptocurrencies");
        }

        User currentUser = authService.getCurrentUser();
        UserCryptoSelection selection = userCryptoSelectionRepository.findByUser(currentUser)
            .orElse(new UserCryptoSelection());

        selection.setUser(currentUser);
        selection.setCryptoIds(cryptoIds);
        
        userCryptoSelectionRepository.save(selection);
    }
}