package com.portfolio.pushpendra.admin.controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.portfolio.pushpendra.admin.model.ProjectModel;
import com.portfolio.pushpendra.admin.service.ProjectService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Controller
public class ProjectController {

    private final ProjectService projectService;
    private final Cloudinary cloudinary;

    public ProjectController(ProjectService projectService, Cloudinary cloudinary) {
        this.projectService = projectService;
        this.cloudinary = cloudinary;
    }

    /** Add new project with Cloudinary upload */
    @PostMapping("/admin/addProject")
    public String addProject(@RequestParam("title") String title,
                             @RequestParam("summary") String summary,
                             @RequestParam("description") String description,
                             @RequestParam("tools") String tools,
                             @RequestParam("features") String features,
                             @RequestParam(value = "imageFile", required = false) MultipartFile image,
                             RedirectAttributes redirectAttributes) {
        try {
            String imageUrl = null;
            String publicId = null;

            if (image != null && !image.isEmpty()) {
                Map uploadResult = cloudinary.uploader().upload(image.getBytes(),
                        ObjectUtils.asMap("folder", "portfolio/projects"));

                imageUrl = (String) uploadResult.get("secure_url");
                publicId = (String) uploadResult.get("public_id");
            }

            ProjectModel project = new ProjectModel();
            project.setTitle(title);
            project.setSummary(summary);
            project.setDescription(description);
            project.setTools(tools);
            project.setFeatures(features);
            project.setImagePath(imageUrl);
            project.setImagePublicId(publicId);

            projectService.saveProject(project);
            redirectAttributes.addFlashAttribute("success", "Project added successfully!");

        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Failed to upload project image: " + e.getMessage());
        }

        return "redirect:/admin/dashboard";
    }

    /** Delete project (also remove from Cloudinary) */
    @PostMapping("/admin/deleteProject/{id}")
    public String deleteProject(@PathVariable Long id,
                                RedirectAttributes redirectAttributes) {
        Optional<ProjectModel> existingOpt = projectService.getProjectById(id);
        if (existingOpt.isPresent()) {
            ProjectModel project = existingOpt.get();
            if (project.getImagePublicId() != null) {
                try {
                    cloudinary.uploader().destroy(project.getImagePublicId(), ObjectUtils.emptyMap());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            projectService.deleteProject(id);
            redirectAttributes.addFlashAttribute("success", "Project deleted successfully!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Project not found!");
        }

        return "redirect:/admin/dashboard";
    }

    /** Update project */
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
                    // ✅ Delete old image if exists
                    if (existing.getImagePublicId() != null) {
                        try {
                            cloudinary.uploader().destroy(existing.getImagePublicId(), ObjectUtils.emptyMap());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    // ✅ Upload new image
                    Map uploadResult = cloudinary.uploader().upload(image.getBytes(),
                            ObjectUtils.asMap("folder", "portfolio/projects"));

                    existing.setImagePath((String) uploadResult.get("secure_url"));
                    existing.setImagePublicId((String) uploadResult.get("public_id"));
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
