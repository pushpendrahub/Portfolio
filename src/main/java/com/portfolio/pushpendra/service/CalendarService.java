package com.portfolio.pushpendra.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.portfolio.pushpendra.model.CalendarModel;
import com.portfolio.pushpendra.model.GoogleTokenModel;
import com.portfolio.pushpendra.model.InterviewBookingModel;
import com.portfolio.pushpendra.repository.CalendarRepo;
import com.portfolio.pushpendra.repository.InterviewBookingRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

/**
 * Service for managing Google Calendar events.
 * Uses stored access & refresh tokens from DB to avoid interactive login.
 */
@Service
public class CalendarService {

    @Autowired
    private CalendarRepo calendarRepo;

    private final InterviewBookingRepo interviewBookingRepo;
    private final SendMailService sendMailService;
    private final InterviewBookingMailService interviewBookingMailService;

    public CalendarService(InterviewBookingRepo interviewBookingRepo, SendMailService sendMailService, InterviewBookingMailService interviewBookingMailService) {
        this.interviewBookingRepo = interviewBookingRepo;
        this.sendMailService = sendMailService;
        this.interviewBookingMailService = interviewBookingMailService;
    }

    /**
     * Builds an authenticated Google Calendar service using stored tokens.
     */
    private com.google.api.services.calendar.Calendar getCalendarService() throws Exception {
        // Load client secrets
        try (InputStream in = getClass().getResourceAsStream("/credentials.json")) {
            if (in == null) {
                throw new IllegalStateException("Missing credentials.json in resources.");
            }

            GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(
                    JacksonFactory.getDefaultInstance(),
                    new InputStreamReader(in)
            );

            // Fetch latest token from DB
            GoogleTokenModel storedToken = calendarRepo.findTopByOrderByIdDesc();
            if (storedToken == null) {
                throw new IllegalStateException("No stored Google token found. Please authenticate first.");
            }

            // Setup OAuth flow
            GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    JacksonFactory.getDefaultInstance(),
                    clientSecrets,
                    Collections.singleton(CalendarScopes.CALENDAR)
            ).setAccessType("offline").build();

            // Create Credential
            Credential credential = new Credential.Builder(flow.getMethod())
                    .setTransport(GoogleNetHttpTransport.newTrustedTransport())
                    .setJsonFactory(JacksonFactory.getDefaultInstance())
                    .setTokenServerEncodedUrl("https://oauth2.googleapis.com/token")
                    .setClientAuthentication(flow.getClientAuthentication())
                    .build();

            // Set tokens
            credential.setAccessToken(storedToken.getAccessToken());
            credential.setRefreshToken(storedToken.getRefreshToken());
            credential.setExpirationTimeMilliseconds(storedToken.getExpiryTimeMillis());

            // Refresh token if expiring in less than 60 sec
            if (credential.getExpiresInSeconds() != null && credential.getExpiresInSeconds() <= 60) {
                if (credential.refreshToken()) {
                    storedToken.setAccessToken(credential.getAccessToken());
                    storedToken.setExpiryTimeMillis(credential.getExpirationTimeMilliseconds());
                    calendarRepo.save(storedToken);
                } else {
                    throw new IllegalStateException("Google token refresh failed. Please re-authenticate.");
                }
            }

            // Return Calendar service
            return new com.google.api.services.calendar.Calendar.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    JacksonFactory.getDefaultInstance(),
                    credential
            ).setApplicationName("Portfolio Booking System").build();
        }
    }

    /**
     * Creates a Google Calendar event for an interview booking.
     */
    public boolean createEvent(CalendarModel request) {
        try {
            com.google.api.services.calendar.Calendar service = getCalendarService();

            // Event start & end times (+1 hour duration)
            DateTime start = new DateTime(request.getDateTime());
            DateTime end = new DateTime(start.getValue() + 60 * 60 * 1000);

            Event event = new Event()
                    .setSummary("Interview with " + request.getRecruiterName())
                    .setDescription(request.getNotes() != null ? request.getNotes() : "No additional notes")
                    .setStart(new EventDateTime().setDateTime(start).setTimeZone("Asia/Kolkata"))
                    .setEnd(new EventDateTime().setDateTime(end).setTimeZone("Asia/Kolkata"));

            // Save event to Google Calendar
            service.events().insert("primary", event).execute();

            //save to DB
            // Parse ISO 8601 date with timezone (e.g., 2025-08-26T10:59:00.000Z)

            InterviewBookingModel interviewBookingModel = new InterviewBookingModel();
            interviewBookingModel.setRecruiterName(request.getRecruiterName());
            interviewBookingModel.setEmail(request.getEmail());

            Instant instant = Instant.parse(request.getDateTime());
            LocalDateTime parsedDateTime = instant.atZone(ZoneId.of("Asia/Kolkata")).toLocalDateTime();

            interviewBookingModel.setDateTime(parsedDateTime);

            interviewBookingModel.setNotes(request.getNotes());
            interviewBookingRepo.save(interviewBookingModel);


            // 3️⃣ Send confirmation emails in background thread (won’t delay response)
            interviewBookingMailService.sendBookingEmails(request);

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
