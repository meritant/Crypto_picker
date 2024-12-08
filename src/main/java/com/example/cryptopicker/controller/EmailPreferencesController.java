package com.example.cryptopicker.controller;

import com.example.cryptopicker.model.EmailPreference;
import com.example.cryptopicker.service.EmailPreferenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/email-preferences")
@RequiredArgsConstructor
public class EmailPreferencesController {
    private final EmailPreferenceService emailPreferenceService;

    @GetMapping
    public EmailPreference getPreferences() {
        return emailPreferenceService.getCurrentUserPreferences();
    }

    @PostMapping
    public EmailPreference savePreferences(@RequestBody EmailPreference preferences) {
        return emailPreferenceService.savePreferences(preferences);
    }

    @PostMapping("/test")
    public void sendTestReport() {
        emailPreferenceService.sendTestReport();
    }
}