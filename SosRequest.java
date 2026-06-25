package com.touristsafety.dto;

import jakarta.validation.constraints.NotNull;

public record SosRequest(
        @NotNull Double latitude,
        @NotNull Double longitude,
        String message
) {
}
