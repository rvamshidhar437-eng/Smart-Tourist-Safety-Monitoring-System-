package com.touristsafety.dto;

import com.touristsafety.entity.IncidentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record IncidentReportRequest(
        @NotNull IncidentType incidentType,
        @NotBlank String description,
        @NotBlank String location,
        String evidencePath
) {
}
