package com.example.cryptopicker.service;

import com.example.cryptopicker.model.EmailPreference;
import com.example.cryptopicker.model.User;
import com.example.cryptopicker.repository.EmailPreferenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EmailPreferenceService {
    private final EmailPreferenceRepository emailPreferenceRepository;
    private final AuthService authService;
    private final EmailService emailService;

    @Transactional
    public EmailPreference getCurrentUserPreferences() {
        User currentUser = authService.getCurrentUser();
        return emailPreferenceRepository.findByUser(currentUser)
            .orElseGet(() -> createDefaultPreferences(currentUser));
    }

    @Transactional
    public EmailPreference savePreferences(EmailPreference preferences) {
        User currentUser = authService.getCurrentUser();
        EmailPreference existingPrefs = emailPreferenceRepository.findByUser(currentUser)
            .orElseGet(() -> createDefaultPreferences(currentUser));
        
        existingPrefs.setDailyReportEnabled(preferences.isDailyReportEnabled());
        existingPrefs.setReportTime(preferences.getReportTime());
        existingPrefs.setReportFormat(preferences.getReportFormat());
        
        return emailPreferenceRepository.save(existingPrefs);
    }

    private EmailPreference createDefaultPreferences(User user) {
        EmailPreference preferences = new EmailPreference();
        preferences.setUser(user);
        return emailPreferenceRepository.save(preferences);
    }

    public void sendTestReport() {
        User currentUser = authService.getCurrentUser();
        emailService.sendTestReport(currentUser);
    }
}