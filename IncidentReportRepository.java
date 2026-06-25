package com.touristsafety.repository;

import com.touristsafety.entity.IncidentReport;
import com.touristsafety.entity.IncidentStatus;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IncidentReportRepository extends JpaRepository<IncidentReport, Long> {

    List<IncidentReport> findAllByOrderByReportTimeDesc();

    List<IncidentReport> findByUserIdOrderByReportTimeDesc(Long userId);

    long countByStatus(IncidentStatus status);

    long countByReportTimeAfter(LocalDateTime reportTime);
}
