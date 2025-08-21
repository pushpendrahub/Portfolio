package com.portfolio.pushpendra.repository;

import com.portfolio.pushpendra.model.GoogleTokenModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CalendarRepo extends JpaRepository<GoogleTokenModel,Long> {

    /**
     * Fetch the stored token details for a specific user.
     * Assuming you store for a single user or use email as identifier.
     */
    GoogleTokenModel findTopByOrderByIdDesc();
}
