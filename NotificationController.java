package com.touristsafety.controller;

import com.touristsafety.dto.NotificationResponse;
import com.touristsafety.entity.User;
import com.touristsafety.service.NotificationService;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public List<NotificationResponse> adminNotifications() {
        return notificationService.latestAdminNotifications();
    }

    @GetMapping("/me")
    public List<NotificationResponse> myNotifications(@AuthenticationPrincipal User currentUser) {
        return notificationService.latestUserNotifications(currentUser.getId());
    }
}
