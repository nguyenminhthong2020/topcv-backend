package com.example.Job.repository;

import com.example.Job.constant.ApplyStatusEnum;
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
            WHERE ja.job.id = :jobId AND (:applyStatus IS NULL OR ja.applyStatus = :applyStatus)
    """)
    Page<JobApply> findJobApplyByJobId(@Param(value = "jobId") Long jobId, @Param(value = "applyStatus") ApplyStatusEnum applyStatus, Pageable pageable);

    @Query("""
            SELECT ja FROM JobApply ja
            JOIN FETCH ja.applicant a
            JOIN FETCH ja.job j
            WHERE j.company.id = :companyId
            AND (:applyStatus IS NULL OR ja.applyStatus = :applyStatus)
            AND (:keyword IS NULL
            OR LOWER(ja.job.name) LIKE LOWER(CONCAT(:keyword,'%'))
            OR LOWER(ja.applicant.name) LIKE LOWER(CONCAT(:keyword,'%'))
            )
    """)
    Page<JobApply> findJobApplyByCompany(@Param(value = "companyId") Long companyId, @Param(value = "keyword") String keyword,  @Param(value = "applyStatus") ApplyStatusEnum applyStatus, Pageable pageable);

}
