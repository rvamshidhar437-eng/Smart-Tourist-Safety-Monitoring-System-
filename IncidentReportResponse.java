package com.touristsafety.dto;

import java.time.LocalDateTime;

public record IncidentReportResponse(
        Long id,
        Long userId,
        String touristName,
        String incidentType,
        String description,
        String location,
        LocalDateTime reportTime,
        String evidencePath,
        String status
) {
}
