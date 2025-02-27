package com.example.Job.controller;

import com.example.Job.models.Result;
import com.example.Job.models.dtos.Test.DecryptRequest;
import com.example.Job.models.dtos.Test.DecryptResponse;
import com.example.Job.models.dtos.Test.EncryptRequest;
import com.example.Job.models.dtos.Test.EncryptResponse;
import com.example.Job.models.dtos.Test.TestChat;
import com.example.Job.service.RedisService;
import com.example.Job.service.Test.TestChatService;
import com.example.Job.service.interfaces.ILogService;

import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Job.utils.DecryptUtil;

import jakarta.validation.Valid;

import org.springframework.core.env.Environment;

@RestController
@RequestMapping("/api/v1/test-no-auth")
public class TestNoAuthController {
    private Environment environment;

    private ILogService _logService;
    private final RedisService _redisService;
    private TestChatService testChatService;

    @Autowired
    public TestNoAuthController(ILogService logServivce, Environment environment, RedisService redisService,
            TestChatService testChatService) {
        this._logService = logServivce;
        this.environment = environment;
        this._redisService = redisService;
        this.testChatService = testChatService;
    }

    @PostMapping("/generate-encrypt-text")
    public EncryptResponse generateEncryptText(@Valid @RequestBody EncryptRequest request) {
        String key = environment.getProperty("app.decrypt-key");
        String plainText = request.plainText;

        String encryptedText = DecryptUtil.encryptString(key, plainText);
        return new EncryptResponse(encryptedText);
    }

    @PostMapping("/generate-decrypt-text")
    public DecryptResponse generateDecryptText(@Valid @RequestBody DecryptRequest request) {
        String key = environment.getProperty("app.decrypt-key");
        Result result = DecryptUtil.decryptString(key, request.encryptText);
        return new DecryptResponse() {
            {
                this.plainText = result.getMessage();
            }
        };
    }

    @PostMapping("/test-set-redis")
    public String TestSetRedis(String key, String value) {
        try {
            // Duration fiveMinutes = Duration.ofMinutes(5);
            Duration thirtySeconds = Duration.ofSeconds(30);
            _redisService.setValue(key, value, thirtySeconds);
            return "Success";
        } catch (Exception ex) {
            // _logService.logError(ex.getMessage(), ex);
            return ex.getMessage();
        }
    }

    @GetMapping("/test-get-redis")
    public String TestGetRedis(String key) {
        try {
            String value = _redisService.getValue(key);
            if (value == null) {
                return "Not found";
            }
            return value;
        } catch (Exception ex) {
            // _logService.logError(ex.getMessage(), ex);
            return ex.getMessage();
        }
    }

    @PostMapping("/test-create-chat")
    public String TestCreateChat() {
        try {
            testChatService.createChat(new TestChat() {
                {
                    this.message = "Hello";
                    this.sender = "Thong 1";
                    this.receiver = "Thong 2";
                    this.timestamp = LocalDateTime.now();
                }
            });
            return "Success";
        } catch (Exception ex) {
            // _logService.logError(ex.getMessage(), ex);
            return ex.getMessage();
        }
    }
}
