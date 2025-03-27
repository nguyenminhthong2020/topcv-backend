package com.example.Job.controller;

import com.example.Job.entity.Company;
import com.example.Job.models.ResultObject;
import com.example.Job.models.dtos.AccountDto;
import com.example.Job.models.dtos.CompanyRegister;
import com.example.Job.models.dtos.ResponseDto;
import com.example.Job.service.interfaces.IAccountService;
import jakarta.validation.Valid;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {
    private IAccountService accountService;

    public AccountController(IAccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public ResponseEntity<ResponseDto> getAllAccounts() {


        List<AccountDto> allAccounts = accountService.getAllAccounts();

        ResponseDto result = ResponseDto.builder()
                .message("Get all accounts successfully")
                .status(HttpStatus.OK)
                .data(allAccounts)
                .build();

        return new ResponseEntity<>(result, HttpStatus.OK);

    }

    @GetMapping("/search")
    public ResponseEntity<ResponseDto> searchAccountByName(@Param(value = "name") String name) {


        List<AccountDto> accountDtos = accountService.searchAccountsByName(name);

        ResponseDto result = ResponseDto.builder()
                .message("Get all accounts successfully")
                .status(HttpStatus.OK)
                .data(accountDtos)
                .build();

        return new ResponseEntity<>(result, HttpStatus.OK);

    }


}
