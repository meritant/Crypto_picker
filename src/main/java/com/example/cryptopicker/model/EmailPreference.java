package com.example.cryptopicker.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "email_preferences")
public class EmailPreference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private boolean dailyReportEnabled = false;
    
    private LocalTime reportTime = LocalTime.of(8, 0); // Default to 8:00 AM

    @Enumerated(EnumType.STRING)
    private ReportFormat reportFormat = ReportFormat.SIMPLE;
}