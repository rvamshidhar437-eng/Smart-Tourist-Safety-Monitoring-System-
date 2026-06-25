package com.touristsafety.repository;

import com.touristsafety.entity.Location;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LocationRepository extends JpaRepository<Location, Long> {

    List<Location> findByUserIdOrderByTimestampDesc(Long userId);

    Optional<Location> findTopByUserIdOrderByTimestampDesc(Long userId);

    @Query("select count(distinct l.user.id) from Location l where l.timestamp > :timestamp")
    long countActiveUsersAfter(@Param("timestamp") LocalDateTime timestamp);
}
