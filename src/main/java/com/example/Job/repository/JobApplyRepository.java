package com.example.Job.repository;

import com.example.Job.entity.JobApply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobApplyRepository extends JpaRepository<JobApply, Long> {
}
