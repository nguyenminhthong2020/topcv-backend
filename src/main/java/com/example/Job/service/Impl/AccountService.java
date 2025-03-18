package com.example.Job.service.Impl;

import com.example.Job.entity.Account;
import com.example.Job.exception.ResourceNotFoundException;
import com.example.Job.repository.AccountRepository;
import com.example.Job.service.interfaces.IAccountService;
import org.springframework.stereotype.Service;

@Service
public class AccountService implements IAccountService {

    private AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Account getAccountByEmail(String email) {
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Account", "email", email));


        return account;
    }
}
