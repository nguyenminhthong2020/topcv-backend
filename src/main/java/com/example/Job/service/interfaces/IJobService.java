package com.example.Job.service.interfaces;

import com.example.Job.models.dtos.JobDto;

public interface IJobService {
    public JobDto createJob(JobDto jobCreateRequest);
}
