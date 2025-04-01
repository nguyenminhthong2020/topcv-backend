package com.example.Job.repository;

import com.example.Job.entity.Job;
import com.example.Job.entity.JobApply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JobApplyRepository extends JpaRepository<JobApply, Long> {

    @Query("""
            SELECT ja FROM JobApply ja
            JOIN FETCH ja.job j
            JOIN FETCH j.company
            WHERE ja.applicant.id = :userId
    """)
    Page<JobApply> findJobApplyByUserId(@Param(value = "userId") Long userId, Pageable pageable);

    @Query("""
            SELECT ja FROM JobApply ja
            JOIN FETCH ja.applicant
            WHERE ja.job.id = :jobId
    """)
    Page<JobApply> findJobApplyByJobId(@Param(value = "jobId") Long jobId, Pageable pageable);

    @Query("""
            SELECT ja FROM JobApply ja
            JOIN FETCH ja.applicant a
            JOIN FETCH ja.job j
            WHERE j.company.id = :companyId
    """)
    Page<JobApply> findJobApplyByCompany(@Param(value = "companyId") Long companyId, Pageable pageable);

}
