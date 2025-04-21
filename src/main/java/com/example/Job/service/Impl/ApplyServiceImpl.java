package com.example.Job.service.Impl;

import com.example.Job.constant.ApplyStatusEnum;
import com.example.Job.entity.Job;
import com.example.Job.entity.JobApply;
import com.example.Job.entity.User;
import com.example.Job.exception.ResourceNotFoundException;
import com.example.Job.exception.StorageException;
import com.example.Job.models.dtos.*;
import com.example.Job.repository.JobApplyRepository;
import com.example.Job.security.JwtUtil;
import com.example.Job.service.IApplyService;
import com.example.Job.service.IStorageService;
import com.example.Job.utils.FileUtil;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;

@Service
public class ApplyServiceImpl implements IApplyService {

    private static final Logger log = LoggerFactory.getLogger(ApplyServiceImpl.class);
    private final ModelMapper modelMapper;
    private final JobApplyRepository jobApplyRepository;
    private final IStorageService storageService;
    private final JwtUtil jwtUtil;
    private final String[] supportedExtensions = {".pdf", ".doc", ".docx"};
    private static final String UPLOAD_FOLDER = "upload/resumes";

    public ApplyServiceImpl(JobApplyRepository jobApplyRepository, IStorageService storageService, JwtUtil jwtUtil, ModelMapper modelMapper) {
        this.jobApplyRepository = jobApplyRepository;
        this.storageService = storageService;
        this.jwtUtil = jwtUtil;
        this.modelMapper = modelMapper;
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

        try{
            return jobApplyRepository.save(jobApply);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException("Error while applying for job. Please try again.");
        }


//        return uploadedResume;

    }

    @Override
    public Page<GetJobApplyResponse> getJobApplyByUser(int currentPage, int pageSize, String sortBy, boolean isAscending) {
        String userId = jwtUtil.extractUserIdFromToken();

        Sort sort = isAscending ? Sort.by(sortBy) : Sort.by(sortBy).descending();

        PageRequest pageRequest = PageRequest.of(currentPage, pageSize, sort);


        Page<JobApply> jobApply = jobApplyRepository.findJobApplyByUserId(Long.parseLong(userId), pageRequest);

        Page<GetJobApplyResponse> jobResPage = jobApply.map(apply -> {

            Job job = apply.getJob();
            GetJobResponse jobResponse = GetJobResponse.builder()
                    .id(job.getId())
                    .salaryFrom(job.getSalaryFrom())
                    .salaryTo(job.getSalaryTo())
                    .city(job.getCity())
                    .name(job.getName())
                    .yearOfExperience(job.getYearOfExperience())
                    .companyName(job.getCompany().getName())
                    .companyImg(job.getCompany().getImgUrl())
                    .companyId(job.getCompany().getId())
                    .build();

            return GetJobApplyResponse.builder()
                    .id(apply.getId())
                    .applyStatus(apply.getApplyStatus())
                    .resume(apply.getResume())
                    .createdAt(apply.getCreatedAt())
                    .updatedAt(apply.getUpdatedAt())
                    .job(jobResponse)
                    .build();
        });

        return jobResPage;
    }

    @Override
    public Page<CVListResponse> getApplyByJob(Long jobId, ApplyStatusEnum applyStatus, int currentPage, int pageSize, String sortBy, boolean isAscending) {

        Sort sort = isAscending ? Sort.by(sortBy) : Sort.by(sortBy).descending();

        PageRequest pageRequest = PageRequest.of(currentPage, pageSize, sort);

        Page<JobApply> jobApply = jobApplyRepository.findJobApplyByJobId(jobId, applyStatus, pageRequest);

        Page<CVListResponse> jobResPage = jobApply.map(apply -> {
            return CVListResponse.builder()
                    .id(apply.getId())
                    .email(apply.getEmail())
                    .resume(apply.getResume())
                    .createdAt(apply.getCreatedAt())
                    .updatedAt(apply.getUpdatedAt())
                    .applyStatus(apply.getApplyStatus())
                    .jobId(apply.getJob().getId())
                    .jobName(apply.getJob().getName())
                    .coverLetter(apply.getCoverLetter())
                    .userName(apply.getApplicant().getName())
                    .build();
        });

        return jobResPage;
    }

    @Override
    public Page<CVListResponse> getJobApplyByCompany(String keyword, ApplyStatusEnum applyStatus,Long companyId, int currentPage, int pageSize, String sortBy, boolean isAscending) {
        Sort sort = isAscending ? Sort.by(sortBy) : Sort.by(sortBy).descending();

        PageRequest pageRequest = PageRequest.of(currentPage, pageSize, sort);

        Page<JobApply> jobApply = jobApplyRepository.findJobApplyByCompany(companyId, keyword != null ? keyword : "", applyStatus, pageRequest);

        Page<CVListResponse> jobResPage = jobApply.map(apply -> {
            return CVListResponse.builder()
                    .id(apply.getId())
                    .email(apply.getEmail())
                    .resume(apply.getResume())
                    .createdAt(apply.getCreatedAt())
                    .updatedAt(apply.getUpdatedAt())
                    .applyStatus(apply.getApplyStatus())
                    .jobId(apply.getJob().getId())
                    .jobName(apply.getJob().getName())
                    .coverLetter(apply.getCoverLetter())
                    .userName(apply.getApplicant().getName())
                    .build();
        });

        return jobResPage;
    }


    @Override
    public ResumeUpdateResponse updateResumeStatus(long resumeId, ApplyStatusEnum applyStatus) {
        JobApply resume = findResumeById(resumeId);

        resume.setApplyStatus(applyStatus);

        try {
            JobApply updatedResume = jobApplyRepository.save(resume);
            return modelMapper.map(updatedResume, ResumeUpdateResponse.class);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException("Error while updating resume status. Please try again.");
        }




    }

    @Override
    public JobApply findResumeById(long resumeId) {
        return jobApplyRepository.findById(resumeId)
                .orElseThrow(() -> new ResourceNotFoundException("Resume", "id", resumeId));
    }


}
