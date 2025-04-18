package com.example.Job.repository;

import com.example.Job.entity.Job;
import com.example.Job.entity.JobSave;
import com.example.Job.entity.Id.JobSaveId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JobSaveRepository extends JpaRepository<JobSave, JobSaveId> {

    @Query("""
            SELECT js.job FROM JobSave js
            JOIN js.job j
            JOIN FETCH j.company
            WHERE js.user.id = :userId
            """)
    Page<Job> findSavedJobByUserId(@Param(value = "userId") Long userId, Pageable pageable);
}
