package com.portfolio.pushpendra.admin.repository;

import com.portfolio.pushpendra.admin.model.VisitorLogModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VisitorLogRepository extends JpaRepository<VisitorLogModel, Long> {

    @Query("SELECT DATE(v.visitTime) AS date, COUNT(v) AS count " +
            "FROM VisitorLogModel v GROUP BY DATE(v.visitTime) ORDER BY date ASC")
    List<Object[]> countVisitorsPerDay();

    // New method for fetching logs in descending order by visit time
    List<VisitorLogModel> findAllByOrderByVisitTimeDesc();
}
