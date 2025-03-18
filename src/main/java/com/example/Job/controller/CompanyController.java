package com.example.Job.controller;

import com.example.Job.entity.Company;
import com.example.Job.models.ResultObject;
import com.example.Job.models.dtos.CompanyRegister;
import com.example.Job.service.interfaces.ICompanyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/companies")
public class CompanyController {
    private ICompanyService companyService;

    @Autowired
    public CompanyController(ICompanyService companyService) {
        this.companyService = companyService;
    }


    @PostMapping
    public ResponseEntity<ResultObject<Company>> createCompany(@Valid @RequestBody CompanyRegister companyRegister) {


        Company registeredCompany = companyService.registerCompany(companyRegister);

        ResultObject<Company> result = new ResultObject<>(true, "Register successfully", HttpStatus.CREATED, registeredCompany);

        return new ResponseEntity<>(result, HttpStatus.CREATED);

    }

}