package com.portfolio.pushpendra.Controller;

import com.portfolio.pushpendra.admin.model.AchievementModel;
import com.portfolio.pushpendra.admin.model.ProfileImageModel;
import com.portfolio.pushpendra.admin.model.ProjectModel;
import com.portfolio.pushpendra.admin.service.AchievementService;
import com.portfolio.pushpendra.admin.service.CertificationService;
import com.portfolio.pushpendra.admin.service.ProfileImageService;
import com.portfolio.pushpendra.admin.service.ProjectService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class AppController {

    @Value("${app.base-url}")
    private String baseUrl;

    private final ProfileImageService profileImageService;
    private final CertificationService certificationService;
    private final ProjectService projectService;
    private final AchievementService achievementService;

    public AppController(ProfileImageService profileImageService, CertificationService certificationService, ProjectService projectService, AchievementService achievementService) {
        this.profileImageService = profileImageService;
        this.certificationService = certificationService;
        this.projectService = projectService;
        this.achievementService = achievementService;
    }

    @GetMapping({"/", "/index"})
    public String indexPage(Model model) {

        ProfileImageModel latest = profileImageService.getLatestProfileImageEntity();

        if (latest != null) {
            model.addAttribute("profileImagePath", latest.getProfileImage());
            model.addAttribute("aboutImagePath", latest.getAboutImage());
            model.addAttribute("resumeImagePath", latest.getResumeImage());
        }
        model.addAttribute("baseUrl", baseUrl);

        //get all certificates
        model.addAttribute("certifications", certificationService.getAllCertifications());

        //get all projects
        List<ProjectModel> projects = projectService.getAllProjects();
        model.addAttribute("projects", projects);

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

        return "index"; // Spring maps this to /templates/index.html
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "/admin/access-denied"; // return name of your Thymeleaf or JSP page
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
