package com.example.Job.service.interfaces;

import com.example.Job.models.dtos.JobApplyRequest;

public interface IApplyService {
    Object applyJob(JobApplyRequest jobApplyRequest);
}
