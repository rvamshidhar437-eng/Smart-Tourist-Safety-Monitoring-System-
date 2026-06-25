package com.touristsafety.controller;

import com.touristsafety.dto.LocationResponse;
import com.touristsafety.dto.LocationUpdateRequest;
import com.touristsafety.entity.Role;
import com.touristsafety.entity.User;
import com.touristsafety.service.LocationService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/location")
public class LocationController {

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @PostMapping("/update")
    public LocationResponse updateLocation(
            @AuthenticationPrincipal User currentUser,
            @Valid @RequestBody LocationUpdateRequest request
    ) {
        return locationService.updateLocation(currentUser, request);
    }

    @GetMapping("/history/{userId}")
    public List<LocationResponse> history(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Long userId
    ) throws AccessDeniedException {
        if (currentUser.getRole() != Role.ADMIN && !currentUser.getId().equals(userId)) {
            throw new AccessDeniedException("You can only view your own location history.");
        }
        return locationService.getHistory(userId);
    }

    @GetMapping("/latest")
    @PreAuthorize("hasRole('ADMIN')")
    public List<LocationResponse> latestLocations() {
        return locationService.getLatestLocations();
    }
}
