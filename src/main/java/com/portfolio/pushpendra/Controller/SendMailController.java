package com.portfolio.pushpendra.controller;

import com.portfolio.pushpendra.service.SendMailService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class SendMailController {

    private final SendMailService mailService;

    public SendMailController(SendMailService mailService) {
        this.mailService = mailService;
    }

    @PostMapping("/sendMail")
    public String sendMail(@RequestParam String name,
                           @RequestParam String email,
                           @RequestParam String subject,
                           @RequestParam String message,
                           RedirectAttributes redirectAttributes) {
        try {
            mailService.receiveContactForm(name, email, subject, message);
            mailService.sendContactForm(name, email, subject, message);

            redirectAttributes.addFlashAttribute("successMessage", "Your message has been sent successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to send your message. Please try again later.");
        }

        return "redirect:/index#contact"; // change "/contact" to your contact page URL mapping
    }

}
