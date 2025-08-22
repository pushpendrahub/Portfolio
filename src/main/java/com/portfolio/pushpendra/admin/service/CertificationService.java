package com.portfolio.pushpendra.admin.service;

import com.portfolio.pushpendra.admin.model.CertificationModel;
import com.portfolio.pushpendra.admin.repository.CertificationRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CertificationService {

    private final CertificationRepo certificationRepo;

    public CertificationService(CertificationRepo certificationRepo) {
        this.certificationRepo = certificationRepo;
    }

    public List<CertificationModel> getAllCertifications() {
        return certificationRepo.findAll();
    }

    public CertificationModel saveCertification(CertificationModel certification) {
        return certificationRepo.save(certification);
    }

    public void deleteCertification(Long id) {
        certificationRepo.deleteById(id);
    }
}
