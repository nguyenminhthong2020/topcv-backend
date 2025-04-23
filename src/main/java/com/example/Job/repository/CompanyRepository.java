package com.example.Job.repository;

import com.example.Job.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CompanyRepository extends JpaRepository<Company, Long> {

    @Modifying
    @Query("UPDATE Company c SET c.numOfFollowers =  c.numOfFollowers + 1 WHERE c.id = :id")
    void increaseFollowers(@Param("id") Long id);
}
