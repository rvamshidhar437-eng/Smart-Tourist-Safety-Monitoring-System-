package com.touristsafety.repository;

import com.touristsafety.entity.Notification;
import com.touristsafety.entity.Role;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findTop10ByRecipientRoleOrderByCreatedAtDesc(Role recipientRole);

    List<Notification> findTop10ByUserIdOrderByCreatedAtDesc(Long userId);
}
