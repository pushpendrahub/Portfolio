package com.portfolio.pushpendra.admin.controller;

import com.portfolio.pushpendra.admin.model.ProfileImageModel;
import com.portfolio.pushpendra.admin.service.ProfileImageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class ProfileImageController {

    private final ProfileImageService profileImageService;

    public ProfileImageController(ProfileImageService profileImageService) {
        this.profileImageService = profileImageService;
    }

    // Example: /var/app/uploads/images   (DON'T put trailing slash in properties)
    @Value("${file.upload-dir}")
    private String uploadDir;

    @PostMapping("/updateProfileImage")
    public String updateProfileImage(@RequestParam("profileImage") MultipartFile image,
                                     RedirectAttributes redirectAttributes) {
        try {
            if (image != null && !image.isEmpty()) {
                // Ensure upload directory exists
                Path dirPath = Paths.get(uploadDir);
                Files.createDirectories(dirPath);

                // Always save as fixed filename
                String fileName = "profileImage.jpg";
                Path filePath = dirPath.resolve(fileName);

                // Overwrite old file (if exists)
                Files.deleteIfExists(filePath);

                // Save new file
                image.transferTo(filePath.toFile());

                // Public URL (fixed path)
                String imageUrl = "/assets/img/profile/" + fileName;

                // Save/update DB
                ProfileImageModel profile = profileImageService.getLatestProfileImageEntity();
                if (profile == null) profile = new ProfileImageModel();
                profile.setProfileImage(imageUrl);
                profileImageService.save(profile);

                // Flash attributes for UI (cacheBuster forces refresh)
                redirectAttributes.addFlashAttribute("profileImagePath", imageUrl);
                redirectAttributes.addFlashAttribute("cacheBuster", System.currentTimeMillis());
            }

            return "redirect:/admin/dashboard";
        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Failed to upload image: " + e.getMessage());
            return "redirect:/admin/dashboard";
        }
    }

}