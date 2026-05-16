package com.portfolio.pushpendra.admin.controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.portfolio.pushpendra.admin.model.ProfileImageModel;
import com.portfolio.pushpendra.admin.service.ProfileImageService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.Map;

@Controller
public class ProfileImageController {

    private final ProfileImageService profileImageService;
    private final Cloudinary cloudinary;

    public ProfileImageController(ProfileImageService profileImageService, Cloudinary cloudinary) {
        this.profileImageService = profileImageService;
        this.cloudinary = cloudinary;
    }

    /* ---------------- PROFILE IMAGE ---------------- */
    @PostMapping("/updateProfileImage")
    public String updateProfileImage(@RequestParam("profileImage") MultipartFile image,
                                     RedirectAttributes redirectAttributes) {
        try {
            if (image != null && !image.isEmpty()) {
                ProfileImageModel profile = profileImageService.getLatestProfileImageEntity();
                if (profile == null) profile = new ProfileImageModel();

                // Delete old if exists
                if (profile.getProfileImageId() != null) {
                    cloudinary.uploader().destroy(profile.getProfileImageId(), ObjectUtils.emptyMap());
                }

                // Upload new
                Map uploadResult = cloudinary.uploader().upload(image.getBytes(),
                        ObjectUtils.asMap("folder", "portfolio/profile"));

                profile.setProfileImage((String) uploadResult.get("secure_url"));
                profile.setProfileImageId((String) uploadResult.get("public_id"));
                profileImageService.save(profile);

                redirectAttributes.addFlashAttribute("successMessage", "Profile image updated successfully!");
                redirectAttributes.addFlashAttribute("profileImagePath", profile.getProfileImage());
                redirectAttributes.addFlashAttribute("cacheBuster", System.currentTimeMillis());
            }
        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("successMessage", "Failed to upload profile image: " + e.getMessage());
        }
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/removeProfileImage")
    public String removeProfileImage(RedirectAttributes ra) {
        ProfileImageModel latest = profileImageService.getLatestProfileImageEntity();
        if (latest != null && latest.getProfileImageId() != null) {
            try {
                cloudinary.uploader().destroy(latest.getProfileImageId(), ObjectUtils.emptyMap());
            } catch (IOException e) {
                e.printStackTrace();
            }
            latest.setProfileImage(null);
            latest.setProfileImageId(null);
            profileImageService.save(latest);
        }
        ra.addFlashAttribute("successMessage", "Profile image removed successfully!");
        ra.addFlashAttribute("profileImagePath", null);
        ra.addFlashAttribute("cacheBuster", System.currentTimeMillis());
        return "redirect:/admin/dashboard";
    }

    /* ---------------- ABOUT IMAGE ---------------- */
    @PostMapping("/updateAboutImage")
    public String updateAboutImage(@RequestParam("aboutImage") MultipartFile image,
                                   RedirectAttributes redirectAttributes) {
        try {
            if (image != null && !image.isEmpty()) {
                ProfileImageModel profile = profileImageService.getLatestProfileImageEntity();
                if (profile == null) profile = new ProfileImageModel();

                if (profile.getAboutImageId() != null) {
                    cloudinary.uploader().destroy(profile.getAboutImageId(), ObjectUtils.emptyMap());
                }

                Map uploadResult = cloudinary.uploader().upload(image.getBytes(),
                        ObjectUtils.asMap("folder", "portfolio/about"));

                profile.setAboutImage((String) uploadResult.get("secure_url"));
                profile.setAboutImageId((String) uploadResult.get("public_id"));
                profileImageService.save(profile);

                redirectAttributes.addFlashAttribute("aboutImagePath", profile.getAboutImage());
                redirectAttributes.addFlashAttribute("cacheBuster", System.currentTimeMillis());
            }
        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Failed to upload about image: " + e.getMessage());
        }
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/removeAboutImage")
    public String removeAboutImage(RedirectAttributes ra) {
        ProfileImageModel latest = profileImageService.getLatestProfileImageEntity();
        if (latest != null && latest.getAboutImageId() != null) {
            try {
                cloudinary.uploader().destroy(latest.getAboutImageId(), ObjectUtils.emptyMap());
            } catch (IOException e) {
                e.printStackTrace();
            }
            latest.setAboutImage(null);
            latest.setAboutImageId(null);
            profileImageService.save(latest);
        }
        ra.addFlashAttribute("aboutImagePath", null);
        ra.addFlashAttribute("cacheBuster", System.currentTimeMillis());
        return "redirect:/admin/dashboard";
    }

    /* ---------------- RESUME IMAGE ---------------- */
    @PostMapping("/updateResumeImage")
    public String updateResumeImage(@RequestParam("resumeImage") MultipartFile image,
                                    RedirectAttributes redirectAttributes) {
        try {
            if (image != null && !image.isEmpty()) {
                ProfileImageModel profile = profileImageService.getLatestProfileImageEntity();
                if (profile == null) profile = new ProfileImageModel();

                if (profile.getResumeImageId() != null) {
                    cloudinary.uploader().destroy(profile.getResumeImageId(), ObjectUtils.emptyMap());
                }

                Map uploadResult = cloudinary.uploader().upload(image.getBytes(),
                        ObjectUtils.asMap("folder", "portfolio/resume"));

                profile.setResumeImage((String) uploadResult.get("secure_url"));
                profile.setResumeImageId((String) uploadResult.get("public_id"));
                profileImageService.save(profile);

                redirectAttributes.addFlashAttribute("resumeImagePath", profile.getResumeImage());
                redirectAttributes.addFlashAttribute("cacheBuster", System.currentTimeMillis());
            }
        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Failed to upload resume image: " + e.getMessage());
        }
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/removeResumeImage")
    public String removeResumeImage(RedirectAttributes ra) {
        ProfileImageModel latest = profileImageService.getLatestProfileImageEntity();
        if (latest != null && latest.getResumeImageId() != null) {
            try {
                cloudinary.uploader().destroy(latest.getResumeImageId(), ObjectUtils.emptyMap());
            } catch (IOException e) {
                e.printStackTrace();
            }
            latest.setResumeImage(null);
            latest.setResumeImageId(null);
            profileImageService.save(latest);
        }
        ra.addFlashAttribute("resumeImagePath", null);
        ra.addFlashAttribute("cacheBuster", System.currentTimeMillis());
        return "redirect:/admin/dashboard";
    }

}