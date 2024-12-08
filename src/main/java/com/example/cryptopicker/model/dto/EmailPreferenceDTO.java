package com.example.cryptopicker.model.dto;

import lombok.Data;
import java.time.LocalTime;
import com.example.cryptopicker.model.ReportFormat;

@Data
public class EmailPreferenceDTO {
    private boolean dailyReportEnabled;
    private LocalTime reportTime;
    private ReportFormat reportFormat;
}