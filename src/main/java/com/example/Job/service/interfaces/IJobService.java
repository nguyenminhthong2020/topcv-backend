package com.example.Job.service.interfaces;

import com.example.Job.models.dtos.JobDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IJobService {
    JobDto createJob(JobDto jobCreateRequest);
    JobDto updateJob(long id, JobDto jobDto);

    Page<JobDto> getAllJobs(int currentPage, int pageSize, String sortBy, boolean isAscending);

    JobDto getJobById(long id);

    List<JobDto> getAllJobsByCompany(int currentPage, int pageSize);
    void deleteJob(long id);
}
