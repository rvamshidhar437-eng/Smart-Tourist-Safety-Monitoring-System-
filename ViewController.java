package com.touristsafety.controller;

import com.touristsafety.dto.RegisterRequest;
import com.touristsafety.dto.UserProfileRequest;
import com.touristsafety.entity.IncidentType;
import com.touristsafety.entity.User;
import com.touristsafety.service.AuthService;
import com.touristsafety.service.DashboardService;
import com.touristsafety.service.IncidentReportService;
import com.touristsafety.service.LocationService;
import com.touristsafety.service.NotificationService;
import com.touristsafety.service.QrCodeService;
import com.touristsafety.service.SafetyTipService;
import com.touristsafety.service.SosService;
import com.touristsafety.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ViewController {

    private final AuthService authService;
    private final DashboardService dashboardService;
    private final LocationService locationService;
    private final SosService sosService;
    private final IncidentReportService incidentReportService;
    private final SafetyTipService safetyTipService;
    private final NotificationService notificationService;
    private final UserService userService;
    private final QrCodeService qrCodeService;
    private final String googleMapsApiKey;

    public ViewController(
            AuthService authService,
            DashboardService dashboardService,
            LocationService locationService,
            SosService sosService,
            IncidentReportService incidentReportService,
            SafetyTipService safetyTipService,
            NotificationService notificationService,
            UserService userService,
            QrCodeService qrCodeService,
            @Value("${app.google-maps.api-key}") String googleMapsApiKey
    ) {
        this.authService = authService;
        this.dashboardService = dashboardService;
        this.locationService = locationService;
        this.sosService = sosService;
        this.incidentReportService = incidentReportService;
        this.safetyTipService = safetyTipService;
        this.notificationService = notificationService;
        this.userService = userService;
        this.qrCodeService = qrCodeService;
        this.googleMapsApiKey = googleMapsApiKey;
    }

    @GetMapping("/")
    public String landing(Model model) {
        model.addAttribute("tips", safetyTipService.getAllTips());
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest("", "", "", ""));
        return "register";
    }

    @PostMapping("/register")
    public String registerSubmit(
            @Valid @ModelAttribute RegisterRequest registerRequest,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return "register";
        }
        authService.register(registerRequest);
        return "redirect:/login?registered";
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication) {
        boolean admin = authentication.getAuthorities().stream()
                .anyMatch(authority -> "ROLE_ADMIN".equals(authority.getAuthority()));
        return admin ? "redirect:/admin/dashboard" : "redirect:/tourist/dashboard";
    }

    @GetMapping("/tourist/dashboard")
    public String touristDashboard(@AuthenticationPrincipal User currentUser, Model model) {
        model.addAttribute("user", currentUser);
        model.addAttribute("googleMapsApiKey", googleMapsApiKey);
        model.addAttribute("incidentTypes", IncidentType.values());
        model.addAttribute("notifications", notificationService.latestUserNotifications(currentUser.getId()));
        model.addAttribute("tips", safetyTipService.getAllTips());
        return "tourist-dashboard";
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard(Model model) {
        model.addAttribute("stats", dashboardService.adminStats());
        model.addAttribute("alerts", sosService.getAllAlerts());
        model.addAttribute("incidents", incidentReportService.getAllReports());
        model.addAttribute("locations", locationService.getLatestLocations());
        model.addAttribute("notifications", notificationService.latestAdminNotifications());
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("googleMapsApiKey", googleMapsApiKey);
        return "admin-dashboard";
    }

    @GetMapping("/profile")
    public String profile(@AuthenticationPrincipal User currentUser, Model model) {
        model.addAttribute("user", currentUser);
        model.addAttribute("profileRequest", new UserProfileRequest(
                currentUser.getName(),
                currentUser.getPhone(),
                currentUser.getEmergencyContactName(),
                currentUser.getEmergencyContactPhone(),
                currentUser.getPreferredLanguage()
        ));
        return "profile";
    }

    @GetMapping(value = "/profile/qr.png", produces = MediaType.IMAGE_PNG_VALUE)
    @ResponseBody
    public byte[] profileQr(@AuthenticationPrincipal User currentUser) {
        return qrCodeService.touristQr(currentUser);
    }

    @PostMapping("/profile")
    public String updateProfile(
            @AuthenticationPrincipal User currentUser,
            @Valid @ModelAttribute UserProfileRequest profileRequest,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("user", currentUser);
            return "profile";
        }
        userService.updateProfile(currentUser, profileRequest);
        return "redirect:/profile?saved";
    }

    @GetMapping("/safety-tips")
    public String safetyTips(Model model) {
        model.addAttribute("tips", safetyTipService.getAllTips());
        return "safety-tips";
    }
}
