package com.touristsafety.controller;

import com.touristsafety.dto.UserProfileRequest;
import com.touristsafety.dto.UserResponse;
import com.touristsafety.entity.User;
import com.touristsafety.service.QrCodeService;
import com.touristsafety.service.UserService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final QrCodeService qrCodeService;

    public UserController(UserService userService, QrCodeService qrCodeService) {
        this.userService = userService;
        this.qrCodeService = qrCodeService;
    }

    @GetMapping
    public List<UserResponse> users() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserResponse user(@PathVariable Long id) {
        return userService.toResponse(userService.getById(id));
    }

    @GetMapping("/me")
    public UserResponse me(@AuthenticationPrincipal User currentUser) {
        return userService.toResponse(currentUser);
    }

    @PutMapping("/profile")
    public UserResponse updateProfile(
            @AuthenticationPrincipal User currentUser,
            @Valid @RequestBody UserProfileRequest request
    ) {
        return userService.updateProfile(currentUser, request);
    }

    @GetMapping(value = "/me/qr", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] qrCode(@AuthenticationPrincipal User currentUser) {
        return qrCodeService.touristQr(currentUser);
    }

    @PatchMapping("/{id}/enabled")
    public UserResponse setEnabled(@PathVariable Long id, @RequestParam boolean enabled) {
        return userService.setEnabled(id, enabled);
    }
}
