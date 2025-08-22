package com.portfolio.pushpendra.admin.repository;

import com.portfolio.pushpendra.admin.model.CertificationModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CertificationRepo extends JpaRepository<CertificationModel,Long> {
}
