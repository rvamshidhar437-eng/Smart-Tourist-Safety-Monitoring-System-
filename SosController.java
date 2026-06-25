package com.touristsafety.controller;

import com.touristsafety.dto.SosAlertResponse;
import com.touristsafety.dto.SosRequest;
import com.touristsafety.entity.SosAlertStatus;
import com.touristsafety.entity.User;
import com.touristsafety.service.SosService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sos")
public class SosController {

    private final SosService sosService;

    public SosController(SosService sosService) {
        this.sosService = sosService;
    }

    @PostMapping("/send")
    public SosAlertResponse sendSos(
            @AuthenticationPrincipal User currentUser,
            @Valid @RequestBody SosRequest request
    ) {
        return sosService.sendSos(currentUser, request);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public List<SosAlertResponse> allAlerts() {
        return sosService.getAllAlerts();
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public SosAlertResponse updateStatus(@PathVariable Long id, @RequestParam SosAlertStatus status) {
        return sosService.updateStatus(id, status);
    }
}
