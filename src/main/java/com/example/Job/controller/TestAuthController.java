package com.example.Job.controller;

import com.example.Job.models.ResultObject;
import com.example.Job.models.ResultPagination;
import com.example.Job.models.dtos.UserDto;
import com.example.Job.service.interfaces.ILogService;
import com.example.Job.service.interfaces.IUserService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/v1/test-auth")
public class TestAuthController {
    private Environment environment;

    private IUserService _userService;

    @Autowired
    public TestAuthController(ILogService logServivce, Environment environment, IUserService userService) {
        this._userService = userService;
        this.environment = environment;
    }

    @SuppressWarnings("rawtypes")
    @GetMapping("/get-all-user")
    public ResponseEntity getAllUser() {
        List<UserDto> users = _userService.getUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/get-all-user-1")
    public ResultObject<List<UserDto>> getAllUser1() {
        List<UserDto> users = _userService.getUsers();
        return new ResultObject<List<UserDto>>(true, "", HttpStatus.OK, users);
    }

    @GetMapping("/get-all-user-pagination")
    public ResultPagination<UserDto> getAllUserPagination(int currentPage, int pageSize) {
        List<UserDto> users = _userService.getUsersByPage(currentPage, pageSize);
        return new ResultPagination<>(true,
                null, HttpStatus.OK, users,
                currentPage, pageSize, -1, -1 );
    }
}
