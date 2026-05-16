package com.portfolio.pushpendra.admin.service;

import com.portfolio.pushpendra.admin.model.ProfileImageModel;
import com.portfolio.pushpendra.admin.repository.ProfileImageRepo;
import org.springframework.stereotype.Service;

@Service
public class ProfileImageService {

    private final ProfileImageRepo profileImageRepo;

    public ProfileImageService(ProfileImageRepo profileImageRepo) {
        this.profileImageRepo = profileImageRepo;
    }

    public ProfileImageModel getLatestProfileImageEntity() {
        return profileImageRepo.findTopByOrderByIdDesc();
    }

    public String getLatestProfileImagePath() {
        ProfileImageModel latest = profileImageRepo.findTopByOrderByIdDesc();
        return (latest != null && latest.getProfileImage() != null && !latest.getProfileImage().isBlank())
                ? latest.getProfileImage()  // ✅ full Cloudinary URL
                : "/assets/img/profile/default-hero.jpg"; // local default
    }

    public String getLatestAboutImagePath() {
        ProfileImageModel latest = profileImageRepo.findTopByOrderByIdDesc();
        return (latest != null && latest.getAboutImage() != null && !latest.getAboutImage().isBlank())
                ? latest.getAboutImage()  // ✅ full Cloudinary URL
                : "/assets/img/profile/default-hero.jpg"; // local default
    }

    public ProfileImageModel save(ProfileImageModel profile) {
        return profileImageRepo.save(profile);
    }

}