package com.portfolio.pushpendra.admin.repository;

import com.portfolio.pushpendra.admin.model.LoginModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoginRepo extends JpaRepository<LoginModel, Long> {

    Optional<LoginModel> findByUsername(String username);
}
