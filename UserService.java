package com.touristsafety.service;

import com.touristsafety.dto.UserProfileRequest;
import com.touristsafety.dto.UserResponse;
import com.touristsafety.entity.Role;
import com.touristsafety.entity.User;
import com.touristsafety.exception.BadRequestException;
import com.touristsafety.exception.ResourceNotFoundException;
import com.touristsafety.repository.UserRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));
    }

    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id + "."));
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public long countTourists() {
        return userRepository.countByRole(Role.TOURIST);
    }

    @Transactional
    public UserResponse updateProfile(User user, UserProfileRequest request) {
        user.setName(request.name().trim());
        user.setPhone(request.phone().trim());
        user.setEmergencyContactName(request.emergencyContactName());
        user.setEmergencyContactPhone(request.emergencyContactPhone());
        user.setPreferredLanguage(request.preferredLanguage() == null || request.preferredLanguage().isBlank()
                ? "en"
                : request.preferredLanguage());
        return toResponse(userRepository.save(user));
    }

    @Transactional
    public UserResponse setEnabled(Long id, boolean enabled) {
        User user = getById(id);
        if (user.getRole() == Role.ADMIN && !enabled) {
            throw new BadRequestException("Admin users cannot be disabled from the dashboard.");
        }
        user.setEnabled(enabled);
        return toResponse(userRepository.save(user));
    }

    public UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getRole().name(),
                user.isEnabled(),
                user.getEmergencyContactName(),
                user.getEmergencyContactPhone(),
                user.getPreferredLanguage(),
                user.getCreatedAt()
        );
    }
}
