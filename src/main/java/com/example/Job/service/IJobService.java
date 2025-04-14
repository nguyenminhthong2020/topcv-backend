package com.example.Job.service;

import com.example.Job.constant.IndustryEnum;
import com.example.Job.constant.JobStatusEnum;
import com.example.Job.constant.JobTypeEnum;
import com.example.Job.constant.LevelEnum;
import com.example.Job.entity.Job;
import com.example.Job.models.dtos.*;
import org.springframework.data.domain.Page;

import java.util.List;


public interface IJobService {
    JobDetailResponse createJob(JobCreateRequest jobCreateRequest);
    JobDetailResponse updateJob(long id, JobDetailResponse jobDetailResponse);

    Page<GetJobResponse> getAllJobs(int currentPage, int pageSize, String sortBy, boolean isAscending);

    JobDetailResponse getJobDetailById(long id);

    JobDetailCompanyResponse getJobDetailWithCompanyById(long id);

    Job getJobById(long id);
    Page<GetJobResponse> getAllJobsByCompany(Long companyId, int currentPage, int pageSize, String sortBy, boolean isAscending);

    Page<JobPostResponse> getAllJobPostsByCompany(Long companyId, JobStatusEnum postStatus, int currentPage, int pageSize, String sortBy, boolean isAscending);
    List<GetJobResponseDto> getRelatedJob(Long jobId, int limit);

    void deleteJob(long id);

    Page<GetJobResponseDto> searchForJobs(String keyword, int currentPage, int pageSize, String sortBy, boolean isAscending,
                                          JobTypeEnum jobType, IndustryEnum industry, LevelEnum level, Integer minExperience, Integer maxExperience,
                                          Double minSalary, Double maxSalary, List<String> cities
                                       );



    void updateJobStatus(UpdateJobStatusRequest request);
}
