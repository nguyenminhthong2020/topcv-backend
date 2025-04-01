package com.example.Job.service.Impl;

import com.example.Job.constant.IndustryEnum;
import com.example.Job.constant.JobStatusEnum;
import com.example.Job.constant.JobTypeEnum;
import com.example.Job.constant.LevelEnum;
import com.example.Job.entity.*;
import com.example.Job.exception.ResourceNotFoundException;
import com.example.Job.models.dtos.*;
import com.example.Job.repository.CompanyRepository;
import com.example.Job.repository.JobRepository;
import com.example.Job.repository.JobSaveRepository;
import com.example.Job.security.JwtUtil;
import com.example.Job.service.IJobService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobService implements IJobService {

    private final JobRepository jobRepository;
    private final ModelMapper modelMapper;
    private final CompanyRepository companyRepository;
    private final JobSaveRepository jobSaveRepository;
    private final JwtUtil jwtUtil;

    public JobService(JobRepository jobRepository, ModelMapper modelMapper, CompanyRepository companyRepository, JobSaveRepository jobSaveRepository, JwtUtil jwtUtil) {
        this.jobRepository = jobRepository;
        this.modelMapper = modelMapper;
        this.companyRepository = companyRepository;
        this.jobSaveRepository = jobSaveRepository;
        this.jwtUtil = jwtUtil;
    }

    @Override
    @Transactional
    public JobDto createJob(JobCreateRequest jobCreateRequest) {
        Company company = companyRepository.findById(jobCreateRequest.getCompanyId())
                .orElseThrow(() ->  new ResourceNotFoundException("Company", "id", jobCreateRequest.getCompanyId()));
//
//        Company company1 = new Company();
//        company1.setId(jobCreateRequest.getCompanyId());
//        Job newJob = modelMapper.map(jobCreateRequest, Job.class);
        Job newJob = Job.builder()
                .jobType(jobCreateRequest.getJobType())
                .jobStatus(jobCreateRequest.getJobStatus() != null ? jobCreateRequest.getJobStatus() : JobStatusEnum.PENDING)
                .level(jobCreateRequest.getLevel())
                .description(jobCreateRequest.getDescription())
                .name(jobCreateRequest.getName())
                .quantity(jobCreateRequest.getQuantity())
                .salaryFrom(jobCreateRequest.getSalaryFrom())
                .salaryTo(jobCreateRequest.getSalaryTo())
                .yearOfExperience(jobCreateRequest.getYearOfExperience())
                .deadline(jobCreateRequest.getDeadline())
                .city(jobCreateRequest.getCity())
                .company(company)
                .skills(jobCreateRequest.getSkills())
                .build();
//        newJob.setCompany(company);

        Job savedJob = jobRepository.save(newJob);

        return modelMapper.map(savedJob,JobDto.class);
    }

    @Override
    public JobDto updateJob(long id, JobDto jobDto) {
        return null;
    }

    @Override
    public Page<GetJobResponse> getAllJobs(int currentPage, int pageSize, String sortBy, boolean isAscending) {

        Sort sort = isAscending ? Sort.by(sortBy) : Sort.by(sortBy).descending();

        PageRequest pageRequest = PageRequest.of(currentPage, pageSize, sort);


        Page<Job> jobPage = jobRepository.findAllWithCompany(pageRequest);

//        Page<GetJobResponse> jobResPage = jobPage.map(job -> modelMapper.map(job, GetJobResponse.class));
//        ExampleMatcher matcher = ExampleMatcher.matching().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)

        Page<GetJobResponse> jobResPage = jobPage.map(job -> {
            return GetJobResponse.builder()
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
        });

        return jobResPage;
    }

    @Override
    public JobDto getJobDetailById(long id) {
        return null;
    }

    @Override
    public Job getJobById(long id) {
        return jobRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job", "id", id));
    }

    @Override
    public Page<GetJobResponse> getAllJobsByCompany(Long companyId, int currentPage, int pageSize, String sortBy, boolean isAscending) {
        Sort sort = isAscending ? Sort.by(sortBy) : Sort.by(sortBy).descending();

        PageRequest pageRequest = PageRequest.of(currentPage, pageSize, sort);


        Page<Job> jobPage = jobRepository.findJobByCompanyId( companyId, pageRequest);

//        Page<GetJobResponse> jobResPage = jobPage.map(job -> modelMapper.map(job, GetJobResponse.class));
//        ExampleMatcher matcher = ExampleMatcher.matching().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)

        Page<GetJobResponse> jobResPage = jobPage.map(job -> {
            return GetJobResponse.builder()
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
        });

        return jobResPage;
    }

    @Override
    public List<GetJobResponseDto> getRelatedJob(Long jobId, int limit) {
        List<GetJobResponseDto> relatedJobs = jobRepository.findRelatedJobsByKeyword(jobId, limit);

        return relatedJobs;
    }


    @Override
    public void deleteJob(long id) {

    }



    @Override
    public Page<GetJobResponseDto> searchForJobs(String keyword, int currentPage, int pageSize, String sortBy, boolean isAscending,
                                                 JobTypeEnum jobType , IndustryEnum industry, LevelEnum level, Integer minExperience, Integer maxExperience,
                                                 Double minSalary, Double maxSalary, List<String> cities
                                              ) {
        Sort sort = isAscending ? Sort.by(sortBy) : Sort.by(sortBy).descending();

        PageRequest pageRequest = PageRequest.of(currentPage, pageSize, sort);

        String citiesParam = cities == null ? null : String.join(",", cities);

        Page<GetJobResponseDto> jobPage = jobRepository.searchJobs(keyword, jobType != null ? jobType.name() : null,
                industry != null ? industry.name() : null, level != null ? level.name() : null,
                minExperience, maxExperience, minSalary, maxSalary, citiesParam ,pageRequest);


        return jobPage;
    }

    @Override
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
            }

        }catch (RuntimeException exception){
            throw new RuntimeException("Error saving job");
        }
    }

    @Override
    public void deleteSavedJob(Long jobId) {
        String userId = jwtUtil.extractUserIdFromToken();

        JobSaveId jobSaveId = new JobSaveId(Long.parseLong( userId), jobId);


        try {
            jobSaveRepository.deleteById(jobSaveId);
        }catch (RuntimeException exception){
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
                    .build();
        });
        return jobResPage;
    }

    @Override
    public void updateJobStatus(UpdateJobStatusRequest request) {
        Job job = getJobById(request.getJobId());

        job.setJobStatus(request.getJobStatus());

        jobRepository.save(job);
    }
}
