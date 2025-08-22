package com.portfolio.pushpendra.admin.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "pf_projects")
public class ProjectModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;          // Project Title
    private String summary;        // Short impact summary
    private String description;    // Full description for modal
    private String icon;           // Bootstrap icon class (e.g. "bi bi-camera")
    private String imagePath;      // Project image path (modal)
    private String tools;          // Comma-separated tools (Java, Spring Boot, MySQL etc.)
    private String features;       // Comma-separated features (list in modal)

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public List<String> getToolsList() {
        if (tools == null || tools.isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.asList(tools.split("\\s*,\\s*"));
        // regex handles spaces after commas
    }


    public void setTools(String tools) {
        this.tools = tools;
    }

    public String getFeatures() {
        return features;
    }

    public void setFeatures(String features) {
        this.features = features;
    }

    public List<String> getFeaturesList() {
        if (features == null || features.isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.asList(features.split("\\s*,\\s*"));
    }

}
