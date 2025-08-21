package com.portfolio.pushpendra.admin.model;
import jakarta.persistence.*;

@Entity
@Table(name = "ProfileImage")
public class ProfileImageModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String profileImage;
    private String aboutImage;
    private String resumeImage;

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
