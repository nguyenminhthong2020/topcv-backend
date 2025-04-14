package com.example.Job.service;

import com.example.Job.models.dtos.GetJobResponse;
import org.springframework.data.domain.Page;

public interface IJobSaveService {
    void saveJob(Long jobId);

    boolean isJobSaved(Long jobId);

    void deleteSavedJob(Long jobId);

    Page<GetJobResponse> getAllSavedJobsByUser(int currentPage, int pageSize, String sortBy, boolean isAscending);
}
