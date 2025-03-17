package com.example.Job.service.Impl;

import com.example.Job.entity.Job;
import com.example.Job.entity.JobApply;
import com.example.Job.entity.User;
import com.example.Job.models.dtos.JobApplyRequest;
import com.example.Job.repository.JobApplyRepository;
import com.example.Job.service.interfaces.IApplyService;
import com.example.Job.service.interfaces.IStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ApplyService implements IApplyService {

    private JobApplyRepository jobApplyRepository;
    private IStorageService storageService;
    private static final String UPLOAD_FOLDER = "upload/resumes";

    public ApplyService(JobApplyRepository jobApplyRepository, IStorageService storageService) {
        this.jobApplyRepository = jobApplyRepository;
        this.storageService = storageService;
    }



    @Override
    public Object applyJob(JobApplyRequest jobApplyRequest) {
        JobApply jobApply = new JobApply();
        jobApply.setEmail(jobApplyRequest.getEmail());
        jobApply.setCoverLetter(jobApplyRequest.getCoverLetter());

        User user = new User();
        user.setId(jobApplyRequest.getUserId());
        Job job = new Job();
        job.setId(jobApplyRequest.getJobId());
        jobApply.setJob(job);
        jobApply.setApplicant(user);

        MultipartFile resumeFile = jobApplyRequest.getResume();

        // Make fileName beginning with the applicant email
        String fileName = jobApplyRequest.getEmail() + "_" + resumeFile.getOriginalFilename();

        Object o = storageService.uploadResumes(resumeFile, UPLOAD_FOLDER, fileName);

        return o;

    }
}
