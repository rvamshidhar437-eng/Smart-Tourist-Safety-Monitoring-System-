package com.touristsafety.dto;

public record SafetyTipResponse(
        Long id,
        String category,
        String title,
        String content
) {
}
