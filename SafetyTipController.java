package com.touristsafety.controller;

import com.touristsafety.dto.SafetyTipResponse;
import com.touristsafety.service.SafetyTipService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/safety-tips")
public class SafetyTipController {

    private final SafetyTipService safetyTipService;

    public SafetyTipController(SafetyTipService safetyTipService) {
        this.safetyTipService = safetyTipService;
    }

    @GetMapping
    public List<SafetyTipResponse> tips(@RequestParam(required = false) String category) {
        return category == null || category.isBlank()
                ? safetyTipService.getAllTips()
                : safetyTipService.getTipsByCategory(category);
    }
}
