package com.touristsafety.dto;

import java.util.List;

public record WeatherResponse(
        Double temperatureCelsius,
        String summary,
        boolean rainExpected,
        List<String> alerts
) {
}
