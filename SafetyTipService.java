package com.touristsafety.service;

import com.touristsafety.dto.SafetyTipResponse;
import com.touristsafety.entity.SafetyTip;
import com.touristsafety.repository.SafetyTipRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class SafetyTipService {

    private final SafetyTipRepository safetyTipRepository;

    public SafetyTipService(SafetyTipRepository safetyTipRepository) {
        this.safetyTipRepository = safetyTipRepository;
    }

    public List<SafetyTipResponse> getAllTips() {
        return safetyTipRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public List<SafetyTipResponse> getTipsByCategory(String category) {
        return safetyTipRepository.findByCategoryIgnoreCase(category).stream()
                .map(this::toResponse)
                .toList();
    }

    private SafetyTipResponse toResponse(SafetyTip tip) {
        return new SafetyTipResponse(tip.getId(), tip.getCategory(), tip.getTitle(), tip.getContent());
    }
}
