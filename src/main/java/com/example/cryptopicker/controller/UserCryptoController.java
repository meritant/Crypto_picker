package com.example.cryptopicker.controller;

import com.example.cryptopicker.service.UserCryptoService;
import com.example.cryptopicker.model.dto.CryptoPriceDTO;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/crypto")
public class UserCryptoController {
    private final UserCryptoService userCryptoService;

    public UserCryptoController(UserCryptoService userCryptoService) {
        this.userCryptoService = userCryptoService;
    }

    @GetMapping("/available")
    public List<CryptoPriceDTO> getAvailableCryptos() {
        return userCryptoService.getAvailableCryptos();
    }

    @GetMapping("/user-selection")
    public List<CryptoPriceDTO> getUserSelection() {
        return userCryptoService.getUserSelection();
    }

    @PostMapping("/user-selection")
    public void saveUserSelection(@RequestBody List<String> cryptoIds) {
        userCryptoService.saveUserSelection(cryptoIds);
    }
}