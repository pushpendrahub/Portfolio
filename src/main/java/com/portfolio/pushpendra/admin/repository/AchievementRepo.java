package com.portfolio.pushpendra.admin.repository;

import com.portfolio.pushpendra.admin.model.AchievementModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AchievementRepo extends JpaRepository<AchievementModel , Long> {
}
