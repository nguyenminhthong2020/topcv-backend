package com.example.Job.repository;

import com.example.Job.entity.Company;
import com.example.Job.entity.CompanyFollow;
import com.example.Job.entity.CompanyFollowId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CompanyFollowRepository extends JpaRepository<CompanyFollow, CompanyFollowId> {

    @Query("SELECT COUNT(cf) FROM CompanyFollow cf WHERE cf.company.id = :companyId")
    long countFollowersByCompanyId(Long companyId);

    @Query("SELECT COUNT(cf) FROM CompanyFollow cf WHERE cf.company = :company")
    long countFollowersByCompany(Company company);
}
