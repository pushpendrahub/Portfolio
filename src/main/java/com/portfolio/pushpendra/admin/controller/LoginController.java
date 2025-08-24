package com.portfolio.pushpendra.admin.controller;

import com.portfolio.pushpendra.admin.model.AchievementModel;
import com.portfolio.pushpendra.admin.model.ProjectModel;
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
    private final AchievementService achievementService;

    public LoginController(VisitorLogService visitorLogService,
                           InterviewBookingService interviewBookingService,
                           ProfileImageService profileImageService, CertificationService certificationService, ProjectService projectService, AchievementService achievementService) {
        this.visitorLogService = visitorLogService;
        this.interviewBookingService = interviewBookingService;
        this.profileImageService = profileImageService;
        this.certificationService = certificationService;
        this.projectService = projectService;
        this.achievementService = achievementService;
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
        List<ProjectModel> projects = projectService.getAllProjects();
        model.addAttribute("projects", projects);
//        model.addAttribute("projects", projectService.getAllProjects());

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

        // Project to icon mapping
        Map<String, String> projectIconMap = new HashMap<>();
        projectIconMap.put("ClassConnect Web App", "bi-journal-code");
        projectIconMap.put("Smart Voting System", "bi-camera");
        projectIconMap.put("Scrap Listing Module", "bi-box-seam");
        projectIconMap.put("Banking Auction Hub", "bi-bank");
        projectIconMap.put("Japanese Auction Model", "bi-currency-yen");

        // ✅ Dynamic project icon assignment
        Map<String, String> finalProjectIconMap = new HashMap<>();
        for (ProjectModel proj : projects) {
            String title = proj.getTitle().toString();
            String techStack = proj.getTools().toString().toLowerCase();

            if (projectIconMap.containsKey(title)) {
                // Direct match with project title
                finalProjectIconMap.put(title, projectIconMap.get(title));
            } else {
                // Fallback: Detect based on technologies
                String iconClass = detectIconFromTech(techStack);
                finalProjectIconMap.put(title, iconClass);
            }
        }

        model.addAttribute("projectIconMap", finalProjectIconMap);
        model.addAttribute("toolIconMap", toolIconMap);

        //getting all achievement
        model.addAttribute("achievements", achievementService.getAllAchievements());
        model.addAttribute("achievement", new AchievementModel()); // ✅

        return "admin/dashboard";
    }

    /**
     * Detect icon class based on technologies used in project.
     */
    private String detectIconFromTech(String tech) {
        tech = tech.toLowerCase();

        if (tech.contains("java") || tech.contains("spring")) return "bi-journal-code text-primary";
        if (tech.contains("python") || tech.contains("ml")) return "bi-terminal-fill text-success";
        if (tech.contains("php")) return "bi-filetype-php text-info";
        if (tech.contains("html")) return "bi-filetype-html text-warning";
        if (tech.contains("css")) return "bi-filetype-css text-secondary";
        if (tech.contains("mysql") || tech.contains("database")) return "bi-database-fill text-danger";
        if (tech.contains("opencv")) return "bi-camera-video-fill text-success";
        if (tech.contains("firebase")) return "bi-cloud-fill text-info";

        // Default fallback
        return "bi-folder-fill text-muted";
    }
}