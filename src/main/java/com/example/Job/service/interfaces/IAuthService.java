package com.example.Job.service.interfaces;

import com.example.Job.models.ResultObject;
import com.example.Job.models.dtos.LoginDto;
import com.example.Job.models.dtos.RegisterDto;
import com.example.Job.models.dtos.UserDto;
import com.example.Job.entity.User;

public interface IAuthService {
    String login(LoginDto loginDto, User user);

    ResultObject<UserDto> registerUser(RegisterDto registerDto);

}
