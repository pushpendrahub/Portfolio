package com.portfolio.pushpendra.admin.service;

import com.portfolio.pushpendra.admin.model.ProjectModel;
import com.portfolio.pushpendra.admin.repository.ProjectRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    private final ProjectRepo projectRepo;

    public ProjectService(ProjectRepo projectRepo) {
        this.projectRepo = projectRepo;
    }

    // ✅ Fetch all projects
    public List<ProjectModel> getAllProjects() {
        return projectRepo.findAll();
    }

    // ✅ Save new / update existing project
    public ProjectModel saveProject(ProjectModel project) {
        return projectRepo.save(project);
    }

    // ✅ Get single project by ID
    public Optional<ProjectModel> getProjectById(Long id) {
        return projectRepo.findById(id);
    }

    // ✅ Delete project
    public void deleteProject(Long id) {
        projectRepo.deleteById(id);
    }

}
