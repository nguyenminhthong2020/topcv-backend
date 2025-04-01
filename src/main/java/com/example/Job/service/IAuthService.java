package com.example.Job.service;

import com.example.Job.entity.Account;
import com.example.Job.models.ResultObject;
import com.example.Job.models.dtos.LoginRequest;
import com.example.Job.models.dtos.RegisterRequest;
import com.example.Job.models.dtos.UserDto;

public interface IAuthService {
    String login(LoginRequest loginRequest, Account user);

    ResultObject<UserDto> registerUser(RegisterRequest registerRequest);

}
