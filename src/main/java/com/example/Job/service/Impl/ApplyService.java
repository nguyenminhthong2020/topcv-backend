package com.example.Job.service.Impl;

import com.example.Job.entity.Job;
import com.example.Job.entity.JobApply;
import com.example.Job.entity.User;
import com.example.Job.exception.StorageException;
import com.example.Job.models.dtos.JobApplyRequest;
import com.example.Job.repository.JobApplyRepository;
import com.example.Job.service.interfaces.IApplyService;
import com.example.Job.service.interfaces.IStorageService;
import com.example.Job.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;

@Service
public class ApplyService implements IApplyService {

    private JobApplyRepository jobApplyRepository;
    private IStorageService storageService;
    private final String[] supportedExtensions = {".pdf", ".doc", ".docx"};
    private static final String UPLOAD_FOLDER = "upload/resumes";

    public ApplyService(JobApplyRepository jobApplyRepository, IStorageService storageService) {
        this.jobApplyRepository = jobApplyRepository;
        this.storageService = storageService;
    }



    @Override
    public Object applyJob(JobApplyRequest jobApplyRequest) throws StorageException {

        MultipartFile resumeFile = jobApplyRequest.getResume();

        if(!FileUtil.isSupportedFileExtension(resumeFile, supportedExtensions)){
            throw new StorageException("Only file type " + Arrays.toString(supportedExtensions) +  " are allowed");
        };

        // Make fileName beginning with the applicant email
        String fileName = jobApplyRequest.getEmail() + "_" + resumeFile.getOriginalFilename();

        Object uploadedResume = storageService.uploadResumes(resumeFile, UPLOAD_FOLDER, fileName);

        User user = new User();
        user.setId(jobApplyRequest.getUserId());
        Job job = new Job();
        job.setId(jobApplyRequest.getJobId());

        JobApply jobApply = new JobApply();
        jobApply.setEmail(jobApplyRequest.getEmail());
        jobApply.setCoverLetter(jobApplyRequest.getCoverLetter());
        jobApply.setJob(job);
        jobApply.setApplicant(user);
        if(uploadedResume instanceof String resumeUrl){
            jobApply.setResume(resumeUrl);
        }

        return jobApplyRepository.save(jobApply);

//        return uploadedResume;

    }
}
