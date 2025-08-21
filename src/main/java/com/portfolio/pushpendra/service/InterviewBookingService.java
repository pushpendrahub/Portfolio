package com.portfolio.pushpendra.service;

import com.portfolio.pushpendra.model.InterviewBookingModel;
import com.portfolio.pushpendra.repository.InterviewBookingRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InterviewBookingService {

    private final InterviewBookingRepo interviewBookingRepo;

    public InterviewBookingService(InterviewBookingRepo interviewBookingRepo) {
        this.interviewBookingRepo = interviewBookingRepo;
    }

    public List<InterviewBookingModel> getAllBookingSlots(){
        return interviewBookingRepo.findAll();
    }
}
