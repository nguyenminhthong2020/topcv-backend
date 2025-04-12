package com.example.Job.service.Impl;

import com.example.Job.config.RedisConfig;
import com.example.Job.entity.Account;
import com.example.Job.exception.ResourceNotFoundException;
import com.example.Job.models.dtos.AccountDto;
import com.example.Job.repository.AccountRepository;
import com.example.Job.service.IAccountService;
import com.example.Job.service.IRedisService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements IAccountService {

    private final AccountRepository accountRepository;
    private final ModelMapper modelMapper;
    private final IRedisService redisService;

    public AccountServiceImpl(AccountRepository accountRepository, ModelMapper modelMapper, IRedisService redisService) {
        this.accountRepository = accountRepository;
        this.modelMapper = modelMapper;
        this.redisService = redisService;
    }

    @Override
    public Account getAccountByEmail(String email) {

        String key = RedisConfig.generateKey(Account.class, "email", email);
        Account cachedAccount = redisService.get(key, Account.class);
        if( cachedAccount != null){
            return cachedAccount;
        }

        Account account = accountRepository.findByEmail(email)
                .orElse(null);

        redisService.set(key, account, Duration.ofHours(24));

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

    @Override
    public Account updateAccount(Account account) {
        return accountRepository.save(account);
    }
}
