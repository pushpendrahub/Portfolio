package com.portfolio.pushpendra.model;


import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "interview_bookings")
public class InterviewBookingModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String recruiterName;
    private String email;
    private LocalDateTime dateTime;
    private String notes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRecruiterName() {
        return recruiterName;
    }

    public void setRecruiterName(String recruiterName) {
        this.recruiterName = recruiterName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "InterviewBookingModel{" +
                "id=" + id +
                ", recruiterName='" + recruiterName + '\'' +
                ", email='" + email + '\'' +
                ", dateTime=" + dateTime +
                ", notes='" + notes + '\'' +
                '}';
    }

}
