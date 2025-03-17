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
import org.springframework.stereotype.Service;

@Service
public class JobService implements IJobService {

    private JobRepository jobRepository;
    private ModelMapper modelMapper;
    private CompanyRepository companyRepository;


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


        Job newJob = modelMapper.map(jobCreateRequest, Job.class);
        newJob.setCompany(company);

        Job savedJob = jobRepository.save(newJob);

        return modelMapper.map(savedJob,JobDto.class);
    }
}
