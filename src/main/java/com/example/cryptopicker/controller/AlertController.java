package com.example.cryptopicker.controller;

import org.springframework.web.bind.annotation.*;
import com.example.cryptopicker.service.AlertService;
import com.example.cryptopicker.model.dto.*;
import org.springframework.security.access.prepost.PreAuthorize;
import java.util.List;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/alerts")
public class AlertController {
    private final AlertService alertService;
    
    public AlertController(AlertService alertService) {
        this.alertService = alertService;
    }
    
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public AlertDTO createAlert(@Valid @RequestBody CreateAlertRequest request) {
        return alertService.createAlert(request);
    }
    
    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public List<AlertDTO> getUserAlerts() {
        return alertService.getUserAlerts();
    }
}