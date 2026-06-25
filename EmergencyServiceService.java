package com.touristsafety.service;

import com.touristsafety.dto.EmergencyServiceResponse;
import com.touristsafety.entity.EmergencyService;
import com.touristsafety.entity.EmergencyServiceType;
import com.touristsafety.repository.EmergencyServiceRepository;
import com.touristsafety.util.GeoUtils;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class EmergencyServiceService {

    private final EmergencyServiceRepository emergencyServiceRepository;

    public EmergencyServiceService(EmergencyServiceRepository emergencyServiceRepository) {
        this.emergencyServiceRepository = emergencyServiceRepository;
    }

    public List<EmergencyServiceResponse> findNearby(
            double latitude,
            double longitude,
            EmergencyServiceType type,
            double radiusKm
    ) {
        List<EmergencyService> services = type == null
                ? emergencyServiceRepository.findAll()
                : emergencyServiceRepository.findByType(type);

        return services.stream()
                .map(service -> toResponse(service, latitude, longitude))
                .filter(service -> service.distanceKm() <= radiusKm)
                .sorted(Comparator.comparing(EmergencyServiceResponse::distanceKm))
                .toList();
    }

    private EmergencyServiceResponse toResponse(EmergencyService service, double latitude, double longitude) {
        double distance = GeoUtils.distanceInKm(
                latitude,
                longitude,
                service.getLatitude(),
                service.getLongitude()
        );
        return new EmergencyServiceResponse(
                service.getId(),
                service.getType().name(),
                service.getName(),
                service.getLatitude(),
                service.getLongitude(),
                service.getContactNumber(),
                service.getAddress(),
                Math.round(distance * 100.0) / 100.0,
                GeoUtils.googleNavigationUrl(service.getLatitude(), service.getLongitude())
        );
    }
}
