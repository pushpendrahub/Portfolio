package com.portfolio.pushpendra.admin.controller;

import com.portfolio.pushpendra.admin.model.AchievementModel;
import com.portfolio.pushpendra.admin.service.AchievementService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
public class AchievementController {

    private final AchievementService achievementService;

    public AchievementController(AchievementService achievementService) {
        this.achievementService = achievementService;
    }

    // ================== ADD Achievement ==================
    @PostMapping("/admin/addAchievement")
    public String addAchievement(@ModelAttribute AchievementModel achievement,
                                 RedirectAttributes redirectAttributes) {
        achievementService.saveAchievement(achievement);
        redirectAttributes.addFlashAttribute("success", "Achievement added successfully!");
        return "redirect:/admin/dashboard";
    }

    // EDIT – open editAchievementModal
    @GetMapping("/admin/editAchievement/{id}")
    public String editAchievementForm(@PathVariable Long id,
                                      RedirectAttributes redirectAttributes) {
        achievementService.getAchievementById(id).ifPresentOrElse(achievement -> {
            redirectAttributes.addFlashAttribute("achievement", achievement);
            redirectAttributes.addFlashAttribute("openModal", "editAchievementModal");
        }, () -> redirectAttributes.addFlashAttribute("error", "Achievement not found!"));

        return "redirect:/admin/dashboard";
    }

    // ================== UPDATE ==================
    @PostMapping("/admin/editAchievement/{id}")
    public String updateAchievement(@PathVariable Long id,
                                    @RequestParam("title") String title,
                                    @RequestParam("description") String description,
                                    RedirectAttributes redirectAttributes) {
        Optional<AchievementModel> existingOpt = achievementService.getAchievementById(id);

        if (existingOpt.isPresent()) {
            AchievementModel achievementModel = existingOpt.get();
            achievementModel.setTitle(title);
            achievementModel.setDescription(description);
            achievementService.saveAchievement(achievementModel);

            redirectAttributes.addFlashAttribute("success", "Achievement updated successfully!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Achievement not found!");
        }

        return "redirect:/admin/dashboard";
    }

    // ================== DELETE ==================
    @PostMapping("/admin/deleteAchievement/{id}")
    public String deleteAchievement(@PathVariable Long id,
                                    RedirectAttributes redirectAttributes) {
        achievementService.deleteAchievement(id);
        redirectAttributes.addFlashAttribute("success", "Achievement deleted successfully!");
        return "redirect:/admin/dashboard";
    }
}