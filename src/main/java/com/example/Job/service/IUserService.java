package com.example.Job.service;

import com.example.Job.models.dtos.UserDto;

import java.util.List;

import com.example.Job.entity.User;

public interface IUserService {
    UserDto updateUser(UserDto userDto, long id);

    void deleteUser(long id);

    UserDto getUserById(long id);

    User getUserByEmail(String email);

    public void updateUserToken(String token, String email);

    public List<UserDto> getUsers();

    public List<UserDto> getUsersByPage(int currentPage, int pageSize);
}
