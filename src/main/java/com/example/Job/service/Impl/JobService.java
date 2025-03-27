package com.example.Job.service.Impl;

import com.example.Job.entity.Company;
import com.example.Job.entity.Job;
import com.example.Job.exception.ResourceNotFoundException;
import com.example.Job.models.dtos.JobDto;
import com.example.Job.repository.CompanyRepository;
import com.example.Job.repository.JobRepository;
import com.example.Job.service.interfaces.IJobService;
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


    public JobService(JobRepository jobRepository, ModelMapper modelMapper, CompanyRepository companyRepository) {
        this.jobRepository = jobRepository;
        this.modelMapper = modelMapper;
        this.companyRepository = companyRepository;
    }


    @Override
    @Transactional
    public JobDto createJob(JobDto jobCreateRequest) {
        Company company = companyRepository.findById(jobCreateRequest.getCompanyId())
                .orElseThrow(() ->  new ResourceNotFoundException("Company", "id", jobCreateRequest.getCompanyId()));
//
//        Company company1 = new Company();
//        company1.setId(jobCreateRequest.getCompanyId());
//        Job newJob = modelMapper.map(jobCreateRequest, Job.class);
        Job newJob = Job.builder()
                .jobType(jobCreateRequest.getJobType())
                .active(jobCreateRequest.isActive())
                .level(jobCreateRequest.getLevel())
                .description(jobCreateRequest.getDescription())
                .name(jobCreateRequest.getName())
                .quantity(jobCreateRequest.getQuantity())
                .salaryFrom(jobCreateRequest.getSalaryFrom())
                .salaryTo(jobCreateRequest.getSalaryTo())
                .yearOfExperience(jobCreateRequest.getYearOfExperience())
                .startDate(jobCreateRequest.getStartDate())
                .endDate(jobCreateRequest.getEndDate())
                .location(jobCreateRequest.getLocation())
                .company(company)
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
    public Page<JobDto> getAllJobs(int currentPage, int pageSize, String sortBy, boolean isAscending) {

        Sort sort = isAscending ? Sort.by(sortBy) : Sort.by(sortBy).descending();

        PageRequest pageRequest = PageRequest.of(currentPage, pageSize, sort);


        Page<Job> jobPage = jobRepository.findAll(pageRequest);

        Page<JobDto> jobResPage = jobPage.map(job -> modelMapper.map(job, JobDto.class));
//        ExampleMatcher matcher = ExampleMatcher.matching().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)


        return jobResPage;
    }

    @Override
    public JobDto getJobById(long id) {
        return null;
    }

    @Override
    public List<JobDto> getAllJobsByCompany(int currentPage, int pageSize) {
        return List.of();
    }

    @Override
    public void deleteJob(long id) {

    }
}
