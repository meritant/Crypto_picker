package com.example.cryptopicker.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import com.example.cryptopicker.model.Alert;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {
    @Value("${spring.mail.username}")
    private String fromEmail;
    
    private final JavaMailSender mailSender;
    
    public void sendAlertEmail(String to, Alert alert) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(to);
        message.setSubject("Crypto Price Alert: " + alert.getCryptoSymbol());
        message.setText(createAlertMessage(alert));
        mailSender.send(message);
    }
    
    private String createAlertMessage(Alert alert) {
        return String.format(
            "Your price alert for %s has been triggered!\n\n" +
            "Target Price: $%.2f\n" +
            "Alert Type: %s\n\n" +
            "Please check your cryptocurrency dashboard for more details.",
            alert.getCryptoSymbol(),
            alert.getTargetPrice(),
            alert.getType()
        );
    }
}