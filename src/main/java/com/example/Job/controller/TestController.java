package com.example.Job.controller;

import com.example.Job.models.dtos.LoginDto;
import com.example.Job.models.dtos.RegisterDto;
import com.example.Job.models.dtos.ResponseDto;
import com.example.Job.models.dtos.UserDto;
import com.example.Job.entity.User;
import com.example.Job.security.JwtAuthResponse;
import com.example.Job.security.JwtTokenProvider;
import com.example.Job.service.AuthService;
import com.example.Job.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import com.example.Job.service.ILogService;

import com.example.Job.annotations.LogRequest;
// import org.springframework.web.bind.annotation.*;

import com.example.Job.models.Result;
import com.example.Job.utils.DecryptUtil;

import org.springframework.core.env.Environment;

import com.example.Job.models.EncryptResponse;

@RestController
@RequestMapping("/api/v1/test")
public class TestController {
    private Environment environment;

    private ILogService _logService;

    @Autowired
    public TestController(ILogService logServivce, Environment environment) {
        this._logService = logServivce;
        this.environment = environment;
    }

    @PostMapping("/generate-encrypt-key")
    public EncryptResponse generateEncryptKey() {
        String key = environment.getProperty("app.decrypt-key");
        String plainText = "123456";

        String encryptedText = DecryptUtil.encryptString(key, plainText);
        return new EncryptResponse(encryptedText);
    }
}
