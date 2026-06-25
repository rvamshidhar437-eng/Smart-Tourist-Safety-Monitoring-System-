package com.touristsafety.config;

import com.touristsafety.entity.EmergencyService;
import com.touristsafety.entity.EmergencyServiceType;
import com.touristsafety.entity.Role;
import com.touristsafety.entity.SafetyTip;
import com.touristsafety.entity.User;
import com.touristsafety.repository.EmergencyServiceRepository;
import com.touristsafety.repository.SafetyTipRepository;
import com.touristsafety.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seedData(
            UserRepository userRepository,
            EmergencyServiceRepository emergencyServiceRepository,
            SafetyTipRepository safetyTipRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {
            seedAdmin(userRepository, passwordEncoder);
            seedEmergencyServices(emergencyServiceRepository);
            seedSafetyTips(safetyTipRepository);
        };
    }

    private void seedAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        userRepository.findByEmail("admin@touristsafety.com").orElseGet(() -> {
            User admin = new User();
            admin.setName("Safety Admin");
            admin.setEmail("admin@touristsafety.com");
            admin.setPhone("9999999999");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(Role.ADMIN);
            return userRepository.save(admin);
        });
    }

    private void seedEmergencyServices(EmergencyServiceRepository repository) {
        if (repository.count() > 0) {
            return;
        }
        repository.save(service(
                EmergencyServiceType.HOSPITAL,
                "AIIMS Emergency Department",
                28.5672,
                77.2100,
                "011-26588500",
                "Ansari Nagar, New Delhi"
        ));
        repository.save(service(
                EmergencyServiceType.POLICE_STATION,
                "Connaught Place Police Station",
                28.6304,
                77.2196,
                "112",
                "Connaught Place, New Delhi"
        ));
        repository.save(service(
                EmergencyServiceType.FIRE_STATION,
                "Delhi Fire Service Headquarters",
                28.6358,
                77.2245,
                "101",
                "Barakhamba Road, New Delhi"
        ));
        repository.save(service(
                EmergencyServiceType.TOURIST_HELP_CENTER,
                "India Tourism Delhi Help Center",
                28.6211,
                77.2183,
                "1363",
                "Janpath, New Delhi"
        ));
    }

    private EmergencyService service(
            EmergencyServiceType type,
            String name,
            double latitude,
            double longitude,
            String contactNumber,
            String address
    ) {
        EmergencyService service = new EmergencyService();
        service.setType(type);
        service.setName(name);
        service.setLatitude(latitude);
        service.setLongitude(longitude);
        service.setContactNumber(contactNumber);
        service.setAddress(address);
        return service;
    }

    private void seedSafetyTips(SafetyTipRepository repository) {
        if (repository.count() > 0) {
            return;
        }
        repository.save(tip(
                "Solo Travelers",
                "Share your itinerary",
                "Keep a trusted contact updated about your route, stay address, and expected arrival times."
        ));
        repository.save(tip(
                "Women Travelers",
                "Prefer verified transport",
                "Use registered taxis, ride-share apps, or hotel-arranged transport during late hours."
        ));
        repository.save(tip(
                "Adventure Travelers",
                "Check weather before starting",
                "Review local forecasts, carry offline maps, and avoid isolated trails during storms or low visibility."
        ));
        repository.save(tip(
                "International Tourists",
                "Keep documents backed up",
                "Store digital copies of passport, visa, insurance, and emergency embassy contacts."
        ));
    }

    private SafetyTip tip(String category, String title, String content) {
        SafetyTip tip = new SafetyTip();
        tip.setCategory(category);
        tip.setTitle(title);
        tip.setContent(content);
        return tip;
    }
}
