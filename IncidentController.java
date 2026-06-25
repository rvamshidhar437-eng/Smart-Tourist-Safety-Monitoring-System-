package com.touristsafety.controller;

import com.touristsafety.dto.IncidentReportRequest;
import com.touristsafety.dto.IncidentReportResponse;
import com.touristsafety.entity.IncidentType;
import com.touristsafety.entity.User;
import com.touristsafety.service.IncidentReportService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/incidents")
public class IncidentController {

    private final IncidentReportService incidentReportService;

    public IncidentController(IncidentReportService incidentReportService) {
        this.incidentReportService = incidentReportService;
    }

    @PostMapping("/report")
    public IncidentReportResponse reportIncident(
            @AuthenticationPrincipal User currentUser,
            @Valid @RequestBody IncidentReportRequest request
    ) {
        return incidentReportService.reportIncident(currentUser, request);
    }

    @PostMapping(value = "/report-with-evidence", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public IncidentReportResponse reportIncidentWithEvidence(
            @AuthenticationPrincipal User currentUser,
            @RequestParam IncidentType incidentType,
            @RequestParam String description,
            @RequestParam String location,
            @RequestParam(required = false) MultipartFile evidence
    ) {
        return incidentReportService.reportIncidentWithEvidence(
                currentUser,
                incidentType,
                description,
                location,
                evidence
        );
    }

    @GetMapping("/mine")
    public List<IncidentReportResponse> myReports(@AuthenticationPrincipal User currentUser) {
        return incidentReportService.getMyReports(currentUser.getId());
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public List<IncidentReportResponse> allReports() {
        return incidentReportService.getAllReports();
    }
}
