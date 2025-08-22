package com.portfolio.pushpendra.admin.controller;

import com.portfolio.pushpendra.admin.model.CertificationModel;
import com.portfolio.pushpendra.admin.service.CertificationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class CertificationController {

    private final CertificationService certificationService;

    @Value("${file.upload-dir-certificates}")
    private String uploadDir;   // Example: D:/uploads/certificates

    public CertificationController(CertificationService certificationService) {
        this.certificationService = certificationService;
    }

    /** Add new certification with image upload */
    @PostMapping("/admin/addCertification")
    public String addCertification(@RequestParam("title") String title,
                                   @RequestParam("category") String category,
                                   @RequestParam("image") MultipartFile image,
                                   RedirectAttributes redirectAttributes) {
        try {
            if (image != null && !image.isEmpty()) {
                // Ensure directory exists
                Path dirPath = Paths.get(uploadDir);
                Files.createDirectories(dirPath);

                // Save image with unique name
                String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
                Path filePath = dirPath.resolve(fileName);
                image.transferTo(filePath.toFile());

                // Public URL
                String imageUrl = "/assets/img/certificates/" + fileName;

                // Save to DB
                CertificationModel certificationModel = new CertificationModel();
                certificationModel.setTitle(title);
                certificationModel.setCategory(category);
                certificationModel.setImagePath(imageUrl);

                certificationService.saveCertification(certificationModel);

                redirectAttributes.addFlashAttribute("success", "Certification added successfully!");
            }
            return "redirect:/admin/dashboard";
        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Failed to upload certificate: " + e.getMessage());
            return "redirect:/admin/dashboard";
        }
    }

    /** Delete certification */
    @GetMapping("admin/deleteCertifications/{id}")
    public String deleteCertification(@PathVariable Long id,
                                      RedirectAttributes redirectAttributes) {
        certificationService.deleteCertification(id);
        redirectAttributes.addFlashAttribute("success", "Certification removed successfully!");
        return "redirect:/admin/dashboard";
    }

}
