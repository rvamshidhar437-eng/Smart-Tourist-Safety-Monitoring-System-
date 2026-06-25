package com.touristsafety.dto;

import java.time.LocalDateTime;

public record SosAlertResponse(
        Long id,
        Long userId,
        String touristName,
        String touristPhone,
        Double latitude,
        Double longitude,
        LocalDateTime alertTime,
        String status,
        String message
) {
}
