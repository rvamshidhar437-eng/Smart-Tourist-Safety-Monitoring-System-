package com.touristsafety.repository;

import com.touristsafety.entity.SosAlert;
import com.touristsafety.entity.SosAlertStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SosAlertRepository extends JpaRepository<SosAlert, Long> {

    List<SosAlert> findAllByOrderByAlertTimeDesc();

    long countByStatus(SosAlertStatus status);
}
