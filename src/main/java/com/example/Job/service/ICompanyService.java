package com.example.Job.service;

import com.example.Job.entity.Company;
import com.example.Job.models.dtos.CompanyRegister;

public interface ICompanyService {
    Company registerCompany(CompanyRegister registerRequest);

}
