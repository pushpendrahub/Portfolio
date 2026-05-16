package com.portfolio.pushpendra.admin.model;
import jakarta.persistence.*;

@Entity
@Table(name = "ProfileImage")
public class ProfileImageModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String profileImage;      // ✅ Cloudinary URL
    private String profileImageId;    // ✅ Cloudinary public_id

    private String aboutImage;        // ✅ URL (Cloudinary)
    private String aboutImageId;      // ✅ public_id

    private String resumeImage;       // ✅ URL (Cloudinary)
    private String resumeImageId;     // ✅ public_id

    public String getProfileImageId() {
        return profileImageId;
    }

    public void setProfileImageId(String profileImageId) {
        this.profileImageId = profileImageId;
    }

    public String getAboutImageId() {
        return aboutImageId;
    }

    public void setAboutImageId(String aboutImageId) {
        this.aboutImageId = aboutImageId;
    }

    public String getResumeImageId() {
        return resumeImageId;
    }

    public void setResumeImageId(String resumeImageId) {
        this.resumeImageId = resumeImageId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getAboutImage() {
        return aboutImage;
    }

    public void setAboutImage(String aboutImage) {
        this.aboutImage = aboutImage;
    }

    public String getResumeImage() {
        return resumeImage;
    }

    public void setResumeImage(String resumeImage) {
        this.resumeImage = resumeImage;
    }
}
