package com.touristsafety.service;

import com.touristsafety.dto.RiskAssessmentResponse;
import com.touristsafety.dto.WeatherResponse;
import com.touristsafety.entity.RiskLevel;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class RiskAssessmentService {

    private final WeatherService weatherService;
    private final IncidentReportService incidentReportService;
    private final SosService sosService;

    public RiskAssessmentService(
            WeatherService weatherService,
            IncidentReportService incidentReportService,
            SosService sosService
    ) {
        this.weatherService = weatherService;
        this.incidentReportService = incidentReportService;
        this.sosService = sosService;
    }

    public RiskAssessmentResponse assess(double latitude, double longitude) {
        int score = 10;
        List<String> reasons = new ArrayList<>();

        WeatherResponse weather = weatherService.currentWeather(latitude, longitude);
        if (weather.rainExpected()) {
            score += 20;
            reasons.add("Rain risk detected near your current location.");
        }
        if (weather.temperatureCelsius() != null && weather.temperatureCelsius() >= 38) {
            score += 15;
            reasons.add("High heat can increase travel and health risk.");
        }

        long recentReports = incidentReportService.countRecentReports();
        if (recentReports > 0) {
            score += Math.min(30, (int) recentReports * 10);
            reasons.add(recentReports + " incident report(s) were submitted in the last 24 hours.");
        }

        long openSos = sosService.countOpenAlerts();
        if (openSos > 0) {
            score += Math.min(30, (int) openSos * 15);
            reasons.add(openSos + " open SOS alert(s) need admin attention.");
        }

        if (reasons.isEmpty()) {
            reasons.add("No active alerts or severe travel signals were found.");
        }

        RiskLevel level = toRiskLevel(score);
        return new RiskAssessmentResponse(level.name(), Math.min(score, 100), reasons);
    }

    private RiskLevel toRiskLevel(int score) {
        if (score >= 80) {
            return RiskLevel.CRITICAL;
        }
        if (score >= 55) {
            return RiskLevel.HIGH;
        }
        if (score >= 30) {
            return RiskLevel.MODERATE;
        }
        return RiskLevel.LOW;
    }
}
