package com.portfolio.pushpendra.service;

import com.portfolio.pushpendra.model.CalendarModel;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Service
public class InterviewBookingMailService {

    private final SendMailService sendMailService;

    public InterviewBookingMailService(SendMailService sendMailService) {
        this.sendMailService = sendMailService;
    }

    @Async
    public void sendBookingEmails(CalendarModel request) {
        Instant instant = Instant.parse(request.getDateTime());
        LocalDateTime localDateTime = instant.atZone(ZoneId.of("Asia/Kolkata")).toLocalDateTime();
        String formattedDateTime = localDateTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
        try {
            // Email to recruiter
            sendMailService.sendContactForm(
                    request.getRecruiterName(),
                    request.getEmail(),
                    "Interview Slot Confirmation",

                    "Your interview slot has been booked successfully for: " + formattedDateTime
            );

            // Email to admin
            sendMailService.receiveContactForm(
                    request.getRecruiterName(),
                    request.getEmail(),
                    "New Interview Booking",
                    "A new interview slot was booked for: " + formattedDateTime +
                            "\nNotes: " + request.getNotes()
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
