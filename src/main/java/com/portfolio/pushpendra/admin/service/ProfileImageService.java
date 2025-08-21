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
                ? latest.getProfileImage()
                : "/assets/img/profile/default-hero.jpg"; // leading slash ✔
    }
    public String getLatestAboutImagePath() {
        ProfileImageModel latest = profileImageRepo.findTopByOrderByIdDesc();
        return (latest != null && latest.getAboutImage() != null && !latest.getAboutImage().isBlank())
                ? latest.getAboutImage()
                : "/assets/img/profile/default-hero.jpg"; // leading slash ✔
    }

    public ProfileImageModel save(ProfileImageModel profile) {
        return profileImageRepo.save(profile);
    }

}