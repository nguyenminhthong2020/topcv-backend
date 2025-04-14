package com.example.Job.service.Impl;

import com.example.Job.config.RedisConfig;
import com.example.Job.entity.Job;
import com.example.Job.entity.JobSave;
import com.example.Job.entity.JobSaveId;
import com.example.Job.entity.User;
import com.example.Job.models.dtos.GetJobResponse;
import com.example.Job.repository.CompanyRepository;
import com.example.Job.repository.JobSaveRepository;
import com.example.Job.security.JwtUtil;
import com.example.Job.service.IJobSaveService;
import com.example.Job.service.IRedisService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class JobSaveServiceImpl implements IJobSaveService {

    private static final Logger log = LoggerFactory.getLogger(JobSaveServiceImpl.class);
    private final ModelMapper modelMapper;
    private final JobSaveRepository jobSaveRepository;
    private final JwtUtil jwtUtil;
    private final IRedisService redisService;


    public JobSaveServiceImpl(ModelMapper modelMapper, JobSaveRepository jobSaveRepository, JwtUtil jwtUtil, IRedisService redisService) {
        this.modelMapper = modelMapper;
        this.jobSaveRepository = jobSaveRepository;
        this.jwtUtil = jwtUtil;
        this.redisService = redisService;
    }

    @Override
    @Transactional
    public void saveJob(Long jobId) {
        String userId = jwtUtil.extractUserIdFromToken();
        User user = new User();
        Job job = new Job();

        user.setId(Long.parseLong(userId));
        job.setId(jobId);

        JobSaveId jobSaveId = new JobSaveId(Long.parseLong( userId), jobId);
        JobSave jobSave = new JobSave(jobSaveId, user, job);


        try {
            if(!jobSaveRepository.existsById(jobSaveId)){
                jobSaveRepository.save(jobSave);

                String key = RedisConfig.generateKey(JobSave.class, "userId", userId);

                redisService.addToSet(key, jobId, true);
            }

        }catch (Exception exception){
            log.error( exception.getMessage());
            throw new RuntimeException("Error saving job");
        }
    }

    @Override
    public boolean isJobSaved(Long jobId) {
        String userId = jwtUtil.extractUserIdFromToken();
        if(userId == null) return false;

        String key = RedisConfig.generateKey(JobSave.class, "userId", userId);
        Boolean isMember = redisService.isMemberOfSet(key, jobId);

        if(isMember != null) return isMember;

        return jobSaveRepository.existsById(new JobSaveId(Long.parseLong( userId), jobId));
    }

    @Override
    @Transactional
    public void deleteSavedJob(Long jobId) {
        String userId = jwtUtil.extractUserIdFromToken();

        JobSaveId jobSaveId = new JobSaveId(Long.parseLong( userId), jobId);


        try {
            jobSaveRepository.deleteById(jobSaveId);

            String key = RedisConfig.generateKey(JobSave.class, "userId", userId);
            redisService.removeFromSet(key, jobId, true);
        }catch (Exception exception){
            log.error(exception.getMessage());
            throw new RuntimeException("Error deleting saved job");
        }



    }

    @Override
    public Page<GetJobResponse> getAllSavedJobsByUser(int currentPage, int pageSize, String sortBy, boolean isAscending) {
        String userId = jwtUtil.extractUserIdFromToken();

        Sort sort = isAscending ? Sort.by(sortBy) : Sort.by(sortBy).descending();

        PageRequest pageRequest = PageRequest.of(currentPage, pageSize, sort);

        Page<Job> savedJobs = jobSaveRepository.findSavedJobByUserId(Long.valueOf(userId), pageRequest);

        Page<GetJobResponse> jobResPage = savedJobs.map(job -> {
            return GetJobResponse.builder()
                    .id(job.getId())
                    .salaryFrom(job.getSalaryFrom())
                    .salaryTo(job.getSalaryTo())
                    .city(job.getCity())
                    .name(job.getName())
                    .yearOfExperience(job.getYearOfExperience())
                    .companyName(job.getCompany().getName())
                    .companyId(job.getCompany().getId())
                    .companyImg(job.getCompany().getImgUrl())
                    .createdAt(job.getCreatedAt())
                    .updatedAt(job.getUpdatedAt())
                    .build();
        });
        return jobResPage;
    }

}
