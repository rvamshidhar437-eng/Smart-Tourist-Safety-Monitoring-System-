package com.touristsafety.dto;

import java.util.List;

public record RiskAssessmentResponse(
        String level,
        int score,
        List<String> reasons
) {
}
