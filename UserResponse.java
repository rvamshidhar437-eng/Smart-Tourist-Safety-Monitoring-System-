package com.touristsafety.dto;

import java.time.LocalDateTime;

public record UserResponse(
        Long id,
        String name,
        String email,
        String phone,
        String role,
        boolean enabled,
        String emergencyContactName,
        String emergencyContactPhone,
        String preferredLanguage,
        LocalDateTime createdAt
) {
}
