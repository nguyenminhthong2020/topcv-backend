package com.example.Job.service;

import com.example.Job.entity.Account;
import com.example.Job.models.dtos.AccountDto;

import java.util.List;

public interface IAccountService {
    Account getAccountByEmail(String email);

    List<AccountDto> getAllAccounts();

    List<AccountDto> searchAccountsByName(String name);

    Account getAccountById(Long id);
}
