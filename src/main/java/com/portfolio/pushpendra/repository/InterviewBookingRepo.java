package com.portfolio.pushpendra.repository;

import com.portfolio.pushpendra.model.InterviewBookingModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterviewBookingRepo extends JpaRepository<InterviewBookingModel,Long> {

    List<InterviewBookingModel> findAll();

}
