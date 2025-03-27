package com.example.Job.service.Impl;

import com.example.Job.entity.Account;
import com.example.Job.exception.ResourceNotFoundException;
import com.example.Job.models.dtos.AccountDto;
import com.example.Job.repository.AccountRepository;
import com.example.Job.service.interfaces.IAccountService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountService implements IAccountService {

    private AccountRepository accountRepository;
    private ModelMapper modelMapper;

    public AccountService(AccountRepository accountRepository, ModelMapper modelMapper) {
        this.accountRepository = accountRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Account getAccountByEmail(String email) {
        Account account = accountRepository.findByEmail(email)
                .orElse(null);


        return account;
    }

    @Override
    public List<AccountDto> getAllAccounts() {
        List<Account> accounts = accountRepository.findAll();

        List<AccountDto> list = accounts.stream().map(account ->
                        modelMapper.map(account, AccountDto.class))
                .collect(Collectors.toList());
        return list;
    }

    @Override
    public List<AccountDto> searchAccountsByName(String name) {
        List<Account> accountsByName = accountRepository.findAccountsByName(name);

        List<AccountDto> list = accountsByName.stream().map(account ->
                        modelMapper.map(account, AccountDto.class))
                .collect(Collectors.toList());

        return list;
    }

    @Override
    public Account getAccountById(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account", "id", id));


        return account;
    }
}
