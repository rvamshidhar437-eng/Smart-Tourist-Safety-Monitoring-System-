package com.touristsafety.repository;

import com.touristsafety.entity.SafetyTip;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SafetyTipRepository extends JpaRepository<SafetyTip, Long> {

    List<SafetyTip> findByCategoryIgnoreCase(String category);
}
