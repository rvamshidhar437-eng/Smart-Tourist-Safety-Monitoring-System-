package com.touristsafety.controller;

import com.touristsafety.dto.EmergencyServiceResponse;
import com.touristsafety.entity.EmergencyServiceType;
import com.touristsafety.service.EmergencyServiceService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/services")
public class EmergencyServiceController {

    private final EmergencyServiceService emergencyServiceService;

    public EmergencyServiceController(EmergencyServiceService emergencyServiceService) {
        this.emergencyServiceService = emergencyServiceService;
    }

    @GetMapping("/nearby")
    public List<EmergencyServiceResponse> nearby(
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam(required = false) EmergencyServiceType type,
            @RequestParam(defaultValue = "25") double radiusKm
    ) {
        return emergencyServiceService.findNearby(latitude, longitude, type, radiusKm);
    }
}
