package com.touristsafety.service;

import com.touristsafety.dto.LocationResponse;
import com.touristsafety.dto.LocationUpdateRequest;
import com.touristsafety.entity.Location;
import com.touristsafety.entity.User;
import com.touristsafety.repository.LocationRepository;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LocationService {

    private final LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Transactional
    public LocationResponse updateLocation(User user, LocationUpdateRequest request) {
        Location location = new Location();
        location.setUser(user);
        location.setLatitude(request.latitude());
        location.setLongitude(request.longitude());
        location.setTimestamp(LocalDateTime.now());
        return toResponse(locationRepository.save(location));
    }

    @Transactional(readOnly = true)
    public List<LocationResponse> getHistory(Long userId) {
        return locationRepository.findByUserIdOrderByTimestampDesc(userId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<LocationResponse> getLatestLocations() {
        Map<Long, Location> latestByUser = new LinkedHashMap<>();
        locationRepository.findAll(Sort.by(Sort.Direction.DESC, "timestamp")).forEach(location ->
                latestByUser.putIfAbsent(location.getUser().getId(), location)
        );
        return latestByUser.values().stream()
                .sorted(Comparator.comparing(Location::getTimestamp).reversed())
                .map(this::toResponse)
                .toList();
    }

    public long countActiveTourists() {
        return locationRepository.countActiveUsersAfter(LocalDateTime.now().minusMinutes(30));
    }

    public LocationResponse toResponse(Location location) {
        User user = location.getUser();
        return new LocationResponse(
                location.getId(),
                user.getId(),
                user.getName(),
                location.getLatitude(),
                location.getLongitude(),
                location.getTimestamp()
        );
    }
}
