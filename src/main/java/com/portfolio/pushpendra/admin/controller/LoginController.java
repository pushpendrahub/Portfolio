package com.portfolio.pushpendra.admin.controller;

import com.portfolio.pushpendra.admin.service.*;
import org.springframework.beans.factory.annotation.Value;
import com.portfolio.pushpendra.admin.model.ProfileImageModel;
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

import java.util.HashMap;
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
    private final CertificationService certificationService;
    private final ProjectService projectService;

    public LoginController(VisitorLogService visitorLogService,
                           InterviewBookingService interviewBookingService,
                           ProfileImageService profileImageService, CertificationService certificationService, ProjectService projectService) {
        this.visitorLogService = visitorLogService;
        this.interviewBookingService = interviewBookingService;
        this.profileImageService = profileImageService;
        this.certificationService = certificationService;
        this.projectService = projectService;
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

        ProfileImageModel about = profileImageService.getLatestProfileImageEntity();

        if (about != null) {
            model.addAttribute("aboutImagePath", about.getAboutImage());
        }
        ProfileImageModel resume = profileImageService.getLatestProfileImageEntity();

        if (resume != null) {
            model.addAttribute("resumeImagePath", resume.getResumeImage());
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

        //get all certificates
        model.addAttribute("certifications", certificationService.getAllCertifications());

        //get all projects
        model.addAttribute("projects", projectService.getAllProjects());

        // Tool to icon mapping
        Map<String, String> toolIconMap = new HashMap<>();
        toolIconMap.put("Java", "bi bi-cup-hot-fill text-primary");
        toolIconMap.put("Spring Boot", "bi bi-box-seam text-success");
        toolIconMap.put("Thymeleaf", "bi bi-file-earmark-code text-warning");
        toolIconMap.put("MySQL", "bi bi-database-fill text-info");
        toolIconMap.put("Spring Security", "bi bi-shield-lock-fill text-danger");

        toolIconMap.put("Python", "bi bi-terminal-fill text-primary");
        toolIconMap.put("OpenCV", "bi bi-camera-video-fill text-success");
        toolIconMap.put("HTML", "bi bi-filetype-html text-warning");
        toolIconMap.put("CSS", "bi bi-filetype-css text-info");
        toolIconMap.put("Firebase", "bi bi-cloud-fill text-danger");

        toolIconMap.put("PHP", "bi bi-filetype-php text-primary");
        toolIconMap.put("CodeIgniter", "bi bi-diagram-3-fill text-success");

        model.addAttribute("toolIconMap", toolIconMap);

        return "admin/dashboard";
    }
}