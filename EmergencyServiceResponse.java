package com.touristsafety.dto;

public record EmergencyServiceResponse(
        Long id,
        String type,
        String name,
        Double latitude,
        Double longitude,
        String contactNumber,
        String address,
        double distanceKm,
        String navigationUrl
) {
}
