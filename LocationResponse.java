package com.touristsafety.dto;

import java.time.LocalDateTime;

public record LocationResponse(
        Long id,
        Long userId,
        String touristName,
        Double latitude,
        Double longitude,
        LocalDateTime timestamp
) {
}
