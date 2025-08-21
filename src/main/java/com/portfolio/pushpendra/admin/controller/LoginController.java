package com.portfolio.pushpendra.admin.controller;

import org.springframework.beans.factory.annotation.Value;
import com.portfolio.pushpendra.admin.model.ProfileImageModel;
import com.portfolio.pushpendra.admin.service.AuthenticationService;
import com.portfolio.pushpendra.admin.service.ProfileImageService;
import com.portfolio.pushpendra.admin.service.VisitorLogService;
import com.portfolio.pushpendra.model.InterviewBookingModel;
import com.portfolio.pushpendra.service.InterviewBookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
@Controller
public class LoginController {

    @Autowired
    private AuthenticationService authenticationService;
    @Value("${app.base-url}")
    private String baseUrl;

    private final VisitorLogService visitorLogService;
    private final InterviewBookingService interviewBookingService;
    private final ProfileImageService profileImageService;

    public LoginController(VisitorLogService visitorLogService,
                           InterviewBookingService interviewBookingService,
                           ProfileImageService profileImageService) {
        this.visitorLogService = visitorLogService;
        this.interviewBookingService = interviewBookingService;
        this.profileImageService = profileImageService;
    }

    @GetMapping("/admin/login")
    public String loginPage() {
        return "admin/login";
    }

    @PostMapping("/admin/login")
    public String loginProcess(@RequestParam String username,
                               @RequestParam String password,
                               Model model) {
        try {
            authenticationService.authenticateAndLogin(username, password);
            return "redirect:/admin/dashboard";
        } catch (UsernameNotFoundException ex) {
            model.addAttribute("error", "Username not found");
            return "admin/login";
        } catch (BadCredentialsException ex) {
            model.addAttribute("error", "Invalid Username or password");
            return "admin/login";
        } catch (Exception ex) {
            model.addAttribute("error", "Login failed: " + ex.getMessage());
            return "admin/login";
        }
    }

    @GetMapping("/admin/dashboard")
    public String dashboard(Model model) {

        ProfileImageModel latest = profileImageService.getLatestProfileImageEntity();

        if (latest != null) {
            model.addAttribute("profileImagePath", latest.getProfileImage());
        }

        // ✅ Pass baseUrl and cacheBuster to Thymeleaf
        model.addAttribute("baseUrl", baseUrl);
        model.addAttribute("cacheBuster", System.currentTimeMillis());

        // ✅ Pass formatted visitor logs
        List<Map<String, String>> visitorLogs = visitorLogService.getFormattedVisitorLogs();
        model.addAttribute("visitors", visitorLogs);

        // ✅ Pass formatted visitor logs
        List<InterviewBookingModel> interviewBooking = interviewBookingService.getAllBookingSlots();
        model.addAttribute("interviewBookings", interviewBooking);

        return "admin/dashboard";
    }
}