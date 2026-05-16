package com.portfolio.pushpendra.admin.controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.portfolio.pushpendra.admin.model.CertificationModel;
import com.portfolio.pushpendra.admin.service.CertificationService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.Map;

@Controller
public class CertificationController {

    private final CertificationService certificationService;
    private final Cloudinary cloudinary;

    public CertificationController(CertificationService certificationService, Cloudinary cloudinary) {
        this.certificationService = certificationService;
        this.cloudinary = cloudinary;
    }

    /** Add new certification with Cloudinary upload */
    @PostMapping("/admin/addCertification")
    public String addCertification(@RequestParam("title") String title,
                                   @RequestParam("category") String category,
                                   @RequestParam("image") MultipartFile image,
                                   RedirectAttributes redirectAttributes) {
        try {
            if (image != null && !image.isEmpty()) {
                // ✅ Upload to Cloudinary (certificates folder)
                Map uploadResult = cloudinary.uploader().upload(image.getBytes(),
                        ObjectUtils.asMap("folder", "portfolio/certificates"));

                String imageUrl = (String) uploadResult.get("secure_url");
                String publicId = (String) uploadResult.get("public_id");

                // ✅ Save to DB
                CertificationModel certificationModel = new CertificationModel();
                certificationModel.setTitle(title);
                certificationModel.setCategory(category);
                certificationModel.setImagePath(imageUrl);
                certificationModel.setImagePublicId(publicId);

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

    /** Delete certification (also remove from Cloudinary) */
    @GetMapping("admin/deleteCertifications/{id}")
    public String deleteCertification(@PathVariable Long id,
                                      RedirectAttributes redirectAttributes) {
        CertificationModel cert = certificationService.getCertificationById(id);
        if (cert != null && cert.getImagePublicId() != null) {
            try {
                // ✅ Delete from Cloudinary
                cloudinary.uploader().destroy(cert.getImagePublicId(), ObjectUtils.emptyMap());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        certificationService.deleteCertification(id);
        redirectAttributes.addFlashAttribute("success", "Certification removed successfully!");
        return "redirect:/admin/dashboard";
    }
}
