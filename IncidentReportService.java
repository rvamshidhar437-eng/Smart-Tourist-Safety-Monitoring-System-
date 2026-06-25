package com.touristsafety.service;

import com.touristsafety.dto.IncidentReportRequest;
import com.touristsafety.dto.IncidentReportResponse;
import com.touristsafety.entity.IncidentReport;
import com.touristsafety.entity.IncidentStatus;
import com.touristsafety.entity.IncidentType;
import com.touristsafety.entity.User;
import com.touristsafety.repository.IncidentReportRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class IncidentReportService {

    private final IncidentReportRepository incidentReportRepository;
    private final NotificationService notificationService;
    private final StorageService storageService;

    public IncidentReportService(
            IncidentReportRepository incidentReportRepository,
            NotificationService notificationService,
            StorageService storageService
    ) {
        this.incidentReportRepository = incidentReportRepository;
        this.notificationService = notificationService;
        this.storageService = storageService;
    }

    @Transactional
    public IncidentReportResponse reportIncident(User user, IncidentReportRequest request) {
        IncidentReport report = new IncidentReport();
        report.setUser(user);
        report.setIncidentType(request.incidentType());
        report.setDescription(request.description());
        report.setLocation(request.location());
        report.setEvidencePath(request.evidencePath());
        return saveReport(user, report);
    }

    @Transactional
    public IncidentReportResponse reportIncidentWithEvidence(
            User user,
            IncidentType incidentType,
            String description,
            String location,
            MultipartFile evidence
    ) {
        IncidentReport report = new IncidentReport();
        report.setUser(user);
        report.setIncidentType(incidentType);
        report.setDescription(description);
        report.setLocation(location);
        report.setEvidencePath(storageService.storeEvidence(evidence));
        return saveReport(user, report);
    }

    @Transactional(readOnly = true)
    public List<IncidentReportResponse> getAllReports() {
        return incidentReportRepository.findAllByOrderByReportTimeDesc().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<IncidentReportResponse> getMyReports(Long userId) {
        return incidentReportRepository.findByUserIdOrderByReportTimeDesc(userId).stream()
                .map(this::toResponse)
                .toList();
    }

    public long countReports() {
        return incidentReportRepository.count();
    }

    public long countRecentReports() {
        return incidentReportRepository.countByReportTimeAfter(LocalDateTime.now().minusHours(24));
    }

    private IncidentReportResponse saveReport(User user, IncidentReport report) {
        IncidentReport savedReport = incidentReportRepository.save(report);
        notificationService.notifyAdmins(
                "Incident reported: " + report.getIncidentType().name(),
                user.getName() + " reported an incident at " + report.getLocation(),
                user
        );
        return toResponse(savedReport);
    }

    public IncidentReportResponse toResponse(IncidentReport report) {
        User user = report.getUser();
        IncidentStatus status = report.getStatus();
        return new IncidentReportResponse(
                report.getId(),
                user.getId(),
                user.getName(),
                report.getIncidentType().name(),
                report.getDescription(),
                report.getLocation(),
                report.getReportTime(),
                report.getEvidencePath(),
                status.name()
        );
    }
}
