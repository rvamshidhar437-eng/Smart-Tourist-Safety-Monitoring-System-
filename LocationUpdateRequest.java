package com.touristsafety.dto;

import jakarta.validation.constraints.NotNull;

public record LocationUpdateRequest(
        @NotNull Double latitude,
        @NotNull Double longitude
) {
}
