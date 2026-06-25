package com.touristsafety.service;

import com.touristsafety.dto.NotificationResponse;
import com.touristsafety.entity.Notification;
import com.touristsafety.entity.Role;
import com.touristsafety.entity.User;
import com.touristsafety.repository.NotificationRepository;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NotificationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationService.class);

    private final NotificationRepository notificationRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public NotificationService(NotificationRepository notificationRepository, SimpMessagingTemplate messagingTemplate) {
        this.notificationRepository = notificationRepository;
        this.messagingTemplate = messagingTemplate;
    }

    @Transactional
    public NotificationResponse notifyAdmins(String title, String message, User relatedUser) {
        Notification notification = new Notification();
        notification.setRecipientRole(Role.ADMIN);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setUser(relatedUser);
        Notification savedNotification = notificationRepository.save(notification);
        NotificationResponse response = toResponse(savedNotification);
        messagingTemplate.convertAndSend("/topic/admin/alerts", response);
        return response;
    }

    @Transactional
    public NotificationResponse notifyTourist(User tourist, String title, String message) {
        Notification notification = new Notification();
        notification.setRecipientRole(Role.TOURIST);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setUser(tourist);
        NotificationResponse response = toResponse(notificationRepository.save(notification));
        messagingTemplate.convertAndSend("/topic/user/" + tourist.getId() + "/notifications", response);
        return response;
    }

    public void notifyEmergencyContact(User tourist, String eventLabel) {
        if (tourist.getEmergencyContactPhone() == null || tourist.getEmergencyContactPhone().isBlank()) {
            return;
        }
        LOGGER.info(
                "Emergency contact notification queued for {} at {} about {}.",
                tourist.getEmergencyContactName(),
                tourist.getEmergencyContactPhone(),
                eventLabel
        );
    }

    public List<NotificationResponse> latestAdminNotifications() {
        return notificationRepository.findTop10ByRecipientRoleOrderByCreatedAtDesc(Role.ADMIN).stream()
                .map(this::toResponse)
                .toList();
    }

    public List<NotificationResponse> latestUserNotifications(Long userId) {
        return notificationRepository.findTop10ByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(this::toResponse)
                .toList();
    }

    private NotificationResponse toResponse(Notification notification) {
        return new NotificationResponse(
                notification.getId(),
                notification.getTitle(),
                notification.getMessage(),
                notification.isReadStatus(),
                notification.getCreatedAt()
        );
    }
}
