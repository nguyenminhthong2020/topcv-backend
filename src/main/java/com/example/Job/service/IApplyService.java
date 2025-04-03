package com.example.Job.service;

import com.example.Job.constant.ApplyStatusEnum;
import com.example.Job.entity.JobApply;
import com.example.Job.exception.StorageException;
import com.example.Job.models.dtos.CVListResponse;
import com.example.Job.models.dtos.GetJobApplyResponse;
import com.example.Job.models.dtos.JobApplyRequest;
import com.example.Job.models.dtos.ResumeUpdateResponse;
import org.springframework.data.domain.Page;

public interface IApplyService {
    Object applyJob(JobApplyRequest jobApplyRequest) throws StorageException;

    Page<GetJobApplyResponse> getJobApplyByUser(int currentPage, int pageSize, String sortBy, boolean isAscending);

    Page<CVListResponse> getApplyByJob(Long jobId, ApplyStatusEnum applyStatus, int currentPage, int pageSize, String sortBy, boolean isAscending);

    Page<CVListResponse> getJobApplyByCompany(String keyword, ApplyStatusEnum applyStatus, Long companyId, int currentPage, int pageSize, String sortBy, boolean isAscending);

    ResumeUpdateResponse updateResumeStatus(long resumeId, ApplyStatusEnum applyStatus);

    JobApply findResumeById(long resumeId);
}
