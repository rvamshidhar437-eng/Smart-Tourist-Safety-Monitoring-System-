package com.touristsafety.dto;

public record DashboardStatsResponse(
        long totalUsers,
        long activeTourists,
        long openSosAlerts,
        long incidentReports
) {
}
