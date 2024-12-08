package com.example.cryptopicker.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Scheduled;
import com.example.cryptopicker.model.*;
import com.example.cryptopicker.model.dto.CryptoPriceDTO;
import com.example.cryptopicker.repository.EmailPreferenceRepository;
import com.example.cryptopicker.repository.UserCryptoSelectionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    @Value("${spring.mail.username}")
    private String fromEmail;
    
    private final JavaMailSender mailSender;
    private final EmailPreferenceRepository emailPreferenceRepository;
    private final UserCryptoSelectionRepository userCryptoSelectionRepository;
    private final CryptoPriceService cryptoPriceService;
    
    @Scheduled(cron = "0 * * * * *") // Run every minute to check for reports to send
    public void checkAndSendDailyReports() {
        LocalTime now = LocalTime.now();
        List<EmailPreference> prefsToSend = emailPreferenceRepository
            .findByDailyReportEnabledTrueAndReportTime(now);
        
        for (EmailPreference pref : prefsToSend) {
            try {
                sendDailyReport(pref.getUser(), pref);
            } catch (Exception e) {
                log.error("Failed to send report to user: " + pref.getUser().getEmail(), e);
            }
        }
    }

    public void sendTestReport(User user) {
        EmailPreference prefs = emailPreferenceRepository.findByUser(user)
            .orElseThrow(() -> new RuntimeException("Email preferences not found"));
        
        sendDailyReport(user, prefs);
    }

    private void sendDailyReport(User user, EmailPreference prefs) {
        UserCryptoSelection selection = userCryptoSelectionRepository.findByUser(user)
            .orElseThrow(() -> new RuntimeException("No cryptocurrencies selected"));

        if (selection.getCryptoIds() == null || selection.getCryptoIds().isEmpty()) {
            log.warn("User {} has no cryptocurrencies selected for report", user.getEmail());
            return;
        }

        List<CryptoPriceDTO> selectedCryptos = selection.getCryptoIds().stream()
            .map(cryptoPriceService::fetchPriceBySymbol)
            .toList();

        String reportContent = prefs.getReportFormat() == ReportFormat.SIMPLE ? 
            createSimpleReport(selectedCryptos, prefs) : 
            createDetailedReport(selectedCryptos, prefs);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(user.getEmail());
        message.setSubject("Your Daily Crypto Report - " + LocalDateTime.now().toLocalDate());
        message.setText(reportContent);
        mailSender.send(message);
        
        log.info("Daily report sent to user: {}", user.getEmail());
    }

    private String createSimpleReport(List<CryptoPriceDTO> cryptos, EmailPreference prefs) {
        StringBuilder report = new StringBuilder("Daily Crypto Summary:\n\n");
        
        for (CryptoPriceDTO crypto : cryptos) {
            report.append(String.format("%s (%s): $%.2f\n",
                crypto.getName(),
                crypto.getSymbol().toUpperCase(),
                crypto.getCurrentPrice()
            ));
        }
        
        return report.toString();
    }

    private String createDetailedReport(List<CryptoPriceDTO> cryptos, EmailPreference prefs) {
        StringBuilder report = new StringBuilder("Detailed Crypto Report:\n\n");
        
        for (CryptoPriceDTO crypto : cryptos) {
            report.append(String.format("--- %s (%s) ---\n", 
                crypto.getName(), 
                crypto.getSymbol().toUpperCase()));
            report.append(String.format("Price: $%.2f\n", crypto.getCurrentPrice()));
            
            if (prefs != null && crypto.getMarketCap() != null) {
                report.append(String.format("Market Cap: $%d\n", crypto.getMarketCap()));
            }
            
            report.append("\n");
        }
        
        return report.toString();
    }

    public void sendAlertEmail(String to, Alert alert) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(to);
        message.setSubject("Crypto Price Alert: " + alert.getCryptoSymbol());
        message.setText(createAlertMessage(alert));
        mailSender.send(message);
        
        log.info("Alert email sent to: {}", to);
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