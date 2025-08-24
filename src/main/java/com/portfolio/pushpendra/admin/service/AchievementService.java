package com.portfolio.pushpendra.admin.service;

import com.portfolio.pushpendra.admin.model.AchievementModel;
import com.portfolio.pushpendra.admin.repository.AchievementRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AchievementService {

    private final AchievementRepo achievementRepo;

    public AchievementService(AchievementRepo achievementRepo) {
        this.achievementRepo = achievementRepo;
    }

    public List<AchievementModel> getAllAchievements(){
        return achievementRepo.findAll();
    }

    public Optional<AchievementModel> getAchievementById(Long id) {
        return achievementRepo.findById(id);
    }

    public AchievementModel saveAchievement(AchievementModel achievementModel){
        return achievementRepo.save(achievementModel);
    }

    public void deleteAchievement(Long id){
        achievementRepo.deleteById(id);
    }
}
