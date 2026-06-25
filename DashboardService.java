package com.touristsafety.service;

import com.touristsafety.dto.DashboardStatsResponse;
import com.touristsafety.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {

    private final UserRepository userRepository;
    private final LocationService locationService;
    private final SosService sosService;
    private final IncidentReportService incidentReportService;

    public DashboardService(
            UserRepository userRepository,
            LocationService locationService,
            SosService sosService,
            IncidentReportService incidentReportService
    ) {
        this.userRepository = userRepository;
        this.locationService = locationService;
        this.sosService = sosService;
        this.incidentReportService = incidentReportService;
    }

    public DashboardStatsResponse adminStats() {
        return new DashboardStatsResponse(
                userRepository.count(),
                locationService.countActiveTourists(),
                sosService.countOpenAlerts(),
                incidentReportService.countReports()
        );
    }
}
