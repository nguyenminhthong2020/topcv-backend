package com.example.Job.service;

import com.example.Job.models.dtos.LoginDto;
import com.example.Job.models.dtos.RegisterDto;
import com.example.Job.models.dtos.UserDto;
import com.example.Job.entity.User;



public interface AuthService {
    String login(LoginDto loginDto, User user);

    UserDto register(RegisterDto registerDto);

}
