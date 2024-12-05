package com.example.cryptopicker.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.cryptopicker.model.Alert;
import com.example.cryptopicker.model.User;
import com.example.cryptopicker.model.AlertStatus;
import com.example.cryptopicker.model.AlertType;
import com.example.cryptopicker.model.dto.AlertDTO;
import com.example.cryptopicker.model.dto.CreateAlertRequest;
import com.example.cryptopicker.model.dto.CryptoPriceDTO;
import com.example.cryptopicker.repository.AlertRepository;
import org.springframework.scheduling.annotation.Scheduled;
import java.util.List;
import java.util.stream.Collectors;
import java.math.BigDecimal;

@Service
@Transactional
public class AlertService {
    private final AlertRepository alertRepository;
    private final AuthService authService;
    private final EmailService emailService;
    private final CryptoPriceService cryptoPriceService;
    
    public AlertService(AlertRepository alertRepository, 
                       AuthService authService,
                       EmailService emailService,
                       CryptoPriceService cryptoPriceService) {
        this.alertRepository = alertRepository;
        this.authService = authService;
        this.emailService = emailService;
        this.cryptoPriceService = cryptoPriceService;
    }
    
    public AlertDTO createAlert(CreateAlertRequest request) {
        User currentUser = authService.getCurrentUser();
        Alert alert = Alert.builder()
            .user(currentUser)
            .cryptoSymbol(request.getCryptoSymbol())
            .targetPrice(request.getTargetPrice())
            .type(request.getType())
            .status(AlertStatus.ACTIVE)
            .build();
        
        return convertToDTO(alertRepository.save(alert));
    }
    
    public List<AlertDTO> getUserAlerts() {
        User currentUser = authService.getCurrentUser();
        return alertRepository.findByUserOrderByIdDesc(currentUser)
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    @Scheduled(fixedRate = 300000) // Check every 5 minutes
    public void checkAlerts() {
        List<Alert> activeAlerts = alertRepository.findByStatus(AlertStatus.ACTIVE);
        for (Alert alert : activeAlerts) {
            CryptoPriceDTO currentPrice = cryptoPriceService.fetchPriceBySymbol(alert.getCryptoSymbol());
            if (shouldTriggerAlert(alert, currentPrice.getCurrentPrice())) {
                emailService.sendAlertEmail(alert.getUser().getEmail(), alert);
                alert.setStatus(AlertStatus.TRIGGERED);
                alertRepository.save(alert);
            }
        }
    }
    
    private boolean shouldTriggerAlert(Alert alert, BigDecimal currentPrice) {
        return (alert.getType() == AlertType.ABOVE && currentPrice.compareTo(alert.getTargetPrice()) > 0) ||
               (alert.getType() == AlertType.BELOW && currentPrice.compareTo(alert.getTargetPrice()) < 0);
    }
    
    private AlertDTO convertToDTO(Alert alert) {
        return new AlertDTO(
            alert.getId(),
            alert.getCryptoSymbol(),
            alert.getTargetPrice(),
            alert.getType(),
            alert.getStatus(),
            alert.getUser().getEmail()
        );
    }
}