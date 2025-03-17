package com.example.Job.service.Impl;

import com.example.Job.entity.Address;
import com.example.Job.entity.Company;
import com.example.Job.models.dtos.CompanyRegister;
import com.example.Job.repository.AccountRepository;
import com.example.Job.repository.CompanyRepository;
import com.example.Job.service.interfaces.ICompanyService;
import org.springframework.stereotype.Service;

@Service
public class CompanyService implements ICompanyService {

    private CompanyRepository companyRepository;
    private AccountRepository accountRepository;

    public CompanyService(CompanyRepository companyRepository, AccountRepository accountRepository) {
        this.companyRepository = companyRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public Company registerCompany(CompanyRegister registerRequest) {

        if (accountRepository.existsByEmail(registerRequest.getEmail())){
            throw new RuntimeException("Email already exists");
        }

        Company company = Company.builder()
                .name(registerRequest.getName())
                .industry(registerRequest.getIndustry())
                .companySize(registerRequest.getCompanySize())
                .companyWebsite(registerRequest.getCompanyWebsite())
                .overview(registerRequest.getOverview())
                .address(new Address(registerRequest.getProvince(), registerRequest.getDistrict(), registerRequest.getLocation()))
                .country(registerRequest.getCountry())
                .imgUrl(registerRequest.getImgUrl())
                .build();

        company.setEmail(registerRequest.getEmail());
        company.setPassword(registerRequest.getPassword());


        Company savedCompany = companyRepository.save(company);

        return savedCompany;
    }
}
