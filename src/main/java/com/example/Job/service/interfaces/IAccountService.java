package com.example.Job.service.interfaces;

import com.example.Job.entity.Account;

public interface IAccountService {
    Account getAccountByEmail(String email);
}
