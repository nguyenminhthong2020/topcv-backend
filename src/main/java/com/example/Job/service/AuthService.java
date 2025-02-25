package com.example.Job.service;

import com.example.Job.dto.LoginDto;
import com.example.Job.dto.RegisterDto;
import com.example.Job.dto.UserDto;
import com.example.Job.entity.User;



public interface AuthService {
    String login(LoginDto loginDto, User user);

    UserDto register(RegisterDto registerDto);

}
