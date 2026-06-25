package com.touristsafety.service;

import com.touristsafety.dto.SosAlertResponse;
import com.touristsafety.dto.SosRequest;
import com.touristsafety.entity.SosAlert;
import com.touristsafety.entity.SosAlertStatus;
import com.touristsafety.entity.User;
import com.touristsafety.exception.ResourceNotFoundException;
import com.touristsafety.repository.SosAlertRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SosService {

    private final SosAlertRepository sosAlertRepository;
    private final NotificationService notificationService;

    public SosService(SosAlertRepository sosAlertRepository, NotificationService notificationService) {
        this.sosAlertRepository = sosAlertRepository;
        this.notificationService = notificationService;
    }

    @Transactional
    public SosAlertResponse sendSos(User user, SosRequest request) {
        SosAlert alert = new SosAlert();
        alert.setUser(user);
        alert.setLatitude(request.latitude());
        alert.setLongitude(request.longitude());
        alert.setMessage(request.message());
        SosAlert savedAlert = sosAlertRepository.save(alert);

        notificationService.notifyAdmins(
                "SOS alert from " + user.getName(),
                "Location: " + request.latitude() + ", " + request.longitude(),
                user
        );
        notificationService.notifyEmergencyContact(user, "SOS alert");
        return toResponse(savedAlert);
    }

    @Transactional(readOnly = true)
    public List<SosAlertResponse> getAllAlerts() {
        return sosAlertRepository.findAllByOrderByAlertTimeDesc().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public SosAlertResponse updateStatus(Long id, SosAlertStatus status) {
        SosAlert alert = sosAlertRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SOS alert not found."));
        alert.setStatus(status);
        return toResponse(sosAlertRepository.save(alert));
    }

    public long countOpenAlerts() {
        return sosAlertRepository.countByStatus(SosAlertStatus.OPEN);
    }

    public SosAlertResponse toResponse(SosAlert alert) {
        User user = alert.getUser();
        return new SosAlertResponse(
                alert.getId(),
                user.getId(),
                user.getName(),
                user.getPhone(),
                alert.getLatitude(),
                alert.getLongitude(),
                alert.getAlertTime(),
                alert.getStatus().name(),
                alert.getMessage()
        );
    }
}
