package com.touristsafety.controller;

import com.touristsafety.dto.RiskAssessmentResponse;
import com.touristsafety.service.RiskAssessmentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/risk")
public class RiskController {

    private final RiskAssessmentService riskAssessmentService;

    public RiskController(RiskAssessmentService riskAssessmentService) {
        this.riskAssessmentService = riskAssessmentService;
    }

    @GetMapping("/current")
    public RiskAssessmentResponse current(@RequestParam double latitude, @RequestParam double longitude) {
        return riskAssessmentService.assess(latitude, longitude);
    }
}
