package com.portfolio.pushpendra.admin.repository;

import com.portfolio.pushpendra.admin.model.ProfileImageModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileImageRepo extends JpaRepository<ProfileImageModel,Long> {

    // fetch the most recent record by ID (auto-incremented)
    ProfileImageModel findTopByOrderByIdDesc();
}
