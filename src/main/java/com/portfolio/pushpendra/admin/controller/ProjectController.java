package com.portfolio.pushpendra.admin.controller;

import org.springframework.beans.factory.annotation.Value;
import com.portfolio.pushpendra.admin.model.ProjectModel;
import com.portfolio.pushpendra.admin.service.ProjectService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Controller
public class ProjectController {

    private final ProjectService projectService;

    @Value("${file.upload-dir-project}")
    private String uploadDir;  // Example: D:/uploads/project

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    /** Add new project with image upload */
    @PostMapping("/admin/addProject")
    public String addProject(@RequestParam("title") String title,
                             @RequestParam("summary") String summary,
                             @RequestParam("description") String description,
                             @RequestParam("tools") String tools,
                             @RequestParam("features") String features,
                             @RequestParam(value = "imageFile", required = false) MultipartFile image, // optional
                             RedirectAttributes redirectAttributes) {
        try {
            String imageUrl = null;

            if (image != null && !image.isEmpty()) {
                Path dirPath = Paths.get(uploadDir);
                Files.createDirectories(dirPath);

                String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
                Path filePath = dirPath.resolve(fileName);
                image.transferTo(filePath.toFile());

                imageUrl = "/assets/img/project/" + fileName;
            }

            ProjectModel project = new ProjectModel();
            project.setTitle(title);
            project.setSummary(summary);
            project.setDescription(description);
            project.setTools(tools);
            project.setFeatures(features);
            project.setImagePath(imageUrl);

            projectService.saveProject(project);
            redirectAttributes.addFlashAttribute("success", "Project added successfully!");

        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Failed to upload project image: " + e.getMessage());
        }

        return "redirect:/admin/dashboard";
    }

    /** Delete project */
    @PostMapping("/admin/deleteProject/{id}")
    public String deleteProject(@PathVariable Long id,
                                RedirectAttributes redirectAttributes) {
        projectService.deleteProject(id);
        redirectAttributes.addFlashAttribute("success", "Project deleted successfully!");
        return "redirect:/admin/dashboard";
    }

    /** Update project */;
    @PostMapping("/admin/editProject/{id}")
    public String updateProject(@PathVariable Long id,
                                @RequestParam("title") String title,
                                @RequestParam("summary") String summary,
                                @RequestParam("description") String description,
                                @RequestParam("tools") String tools,
                                @RequestParam("features") String features,
                                @RequestParam(value = "imageFile", required = false) MultipartFile image,
                                RedirectAttributes redirectAttributes) {
        try {
            Optional<ProjectModel> existingOpt = projectService.getProjectById(id);

            if (existingOpt.isPresent()) {
                ProjectModel existing = existingOpt.get();
                existing.setTitle(title);
                existing.setSummary(summary);
                existing.setDescription(description);
                existing.setTools(tools);
                existing.setFeatures(features);

                if (image != null && !image.isEmpty()) {
                    Path dirPath = Paths.get(uploadDir);
                    Files.createDirectories(dirPath);

                    String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
                    Path filePath = dirPath.resolve(fileName);
                    image.transferTo(filePath.toFile());

                    existing.setImagePath("/assets/img/project/" + fileName);
                }

                projectService.saveProject(existing);
                redirectAttributes.addFlashAttribute("success", "Project updated successfully!");
            } else {
                redirectAttributes.addFlashAttribute("error", "Project not found!");
            }

        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Failed to update project image: " + e.getMessage());
        }

        return "redirect:/admin/dashboard";
    }

}
