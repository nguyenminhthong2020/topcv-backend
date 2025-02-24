package com.example.Job.service;


import com.example.Job.dto.UserDto;
import com.example.Job.entity.User;

public interface UserService {
        UserDto updateUser(UserDto userDto, long id);

        void deleteUser(long id);

        UserDto getUserById(long id);

        User getUserByEmail(String email);

        public void updateUserToken(String token, String email);
    }


