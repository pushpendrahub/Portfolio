package com.portfolio.pushpendra.admin.repository;

import com.portfolio.pushpendra.admin.model.ProjectModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepo extends JpaRepository<ProjectModel ,Long> {
}
