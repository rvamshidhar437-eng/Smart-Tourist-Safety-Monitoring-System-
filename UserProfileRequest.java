package com.touristsafety.dto;

import jakarta.validation.constraints.NotBlank;

public record UserProfileRequest(
        @NotBlank String name,
        @NotBlank String phone,
        String emergencyContactName,
        String emergencyContactPhone,
        String preferredLanguage
) {
}
