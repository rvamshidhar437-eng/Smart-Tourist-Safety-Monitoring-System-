package com.touristsafety.repository;

import com.touristsafety.entity.EmergencyService;
import com.touristsafety.entity.EmergencyServiceType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmergencyServiceRepository extends JpaRepository<EmergencyService, Long> {

    List<EmergencyService> findByType(EmergencyServiceType type);
}
