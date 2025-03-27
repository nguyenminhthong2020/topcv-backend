package com.example.Job.controller;

import com.example.Job.entity.Account;
import com.example.Job.models.dtos.LoginDto;
import com.example.Job.models.dtos.RegisterDto;
import com.example.Job.models.dtos.ResponseDto;
import com.example.Job.models.dtos.UserDto;
import com.example.Job.entity.User;
import com.example.Job.security.JwtAuthResponse;
import com.example.Job.security.JwtTokenProvider;
import com.example.Job.service.interfaces.IAccountService;
import com.example.Job.service.interfaces.IAuthService;
import com.example.Job.service.interfaces.ILogService;
import com.example.Job.service.interfaces.IUserService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;

import java.time.LocalDateTime;

import com.example.Job.annotations.LogRequest;
// import org.springframework.web.bind.annotation.*;

import com.example.Job.models.Result;
import com.example.Job.models.ResultObject;
import com.example.Job.utils.DecryptUtil;

import org.springframework.core.env.Environment;

@RestController
@RequestMapping("/api/v1/auth")
@LogRequest
public class AuthController {

    private IAuthService authService;
    private IUserService userService;
    private IAccountService accountService;
    private JwtTokenProvider jwtTokenProvider;
    private Environment environment;

    private ILogService _logService;

    @Value("${app.jwt.refresh-token-expiration-seconds}")
    private long refreshTokenExpiration;

    @Autowired
    public AuthController(IAuthService authService, IUserService userService,
            JwtTokenProvider jwtTokenProvider, ILogService logServivce, Environment environment, IAccountService accountService) {
        this.authService = authService;
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
        this._logService = logServivce;
        this.environment = environment;
        this.accountService = accountService;
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDto> login(@Valid @RequestBody LoginDto loginDto) {

//        Account user = userService.getUserByEmail(loginDto.getUsernameOrEmail());
        Account user = accountService.getAccountByEmail(loginDto.getUsernameOrEmail());

        if(user == null){
            throw new BadCredentialsException("Invalid email or password");
        }
        JwtAuthResponse.UserLogin userLogin = new JwtAuthResponse.UserLogin(
                user.getId(), user.getEmail(), user.getName(), user.getRole());

        String accessToken = authService.login(loginDto, user);

        JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();
        jwtAuthResponse.setAccessToken(accessToken);
        jwtAuthResponse.setUserLogin(userLogin);

//        String refreshToken = jwtTokenProvider.createRefreshToken(loginDto.getUsernameOrEmail(), jwtAuthResponse);

//        userService.updateUserToken(refreshToken, loginDto.getUsernameOrEmail());

        // ResponseCookie responseCookie = ResponseCookie
        // .from("refreshToken", refreshToken)
        // .httpOnly(true)
        // .path("/")
        // .maxAge(refreshTokenExpiration)
        // .build();
        ResponseDto response = ResponseDto.builder()
                .message("Login successfully")
                .status(HttpStatus.OK)
                .data(jwtAuthResponse)
                .build();
        return ResponseEntity.ok()
                // .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .body(response);

    }

    @PostMapping("/register")
    public ResponseEntity<ResponseDto> register(@Valid @RequestBody RegisterDto registerDto) {
        StringBuilder sb = new StringBuilder();
        try {
            // Tạo ObjectMapper để chuyển RegisterDto thành chuỗi JSON
            ObjectMapper objectMapper = new ObjectMapper();
            String registerDtoJson = objectMapper.writeValueAsString(registerDto);

            // Thêm vào StringBuilder
            sb.append(String.format("New user register at %s: \n", LocalDateTime.now()))
                    .append(registerDtoJson)
                    .append("\n");

            // System.out.println("RegisterDto: " + registerDto);
            ResultObject<UserDto> response = authService.registerUser(registerDto);

            if (response.isSuccess == false) {
                ResponseDto errorResponseDto = new ResponseDto.Builder()
                        .setStatus(response.httpStatus)
                        .setMessage("Registration failed: " + response.message)
                        .setData(null)
                        .build();

                // Trả về ResponseEntity với lỗi
                return new ResponseEntity<>(errorResponseDto, response.httpStatus);
            }

            ResponseDto responseDto = new ResponseDto.Builder()
                    .setStatus(HttpStatus.CREATED)
                    .setMessage("Register successfully")
                    .setData(response.data)
                    .build();

            sb.append("\r\tRegister successfully");

            _logService.logInfo(sb.toString());

            return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
        } catch (Exception e) { // sẽ fix lại chỗ try catch này
//            e.printStackTrace();

            // Ghi log lỗi
            _logService.logError("Registration error", e);

            // Tạo ResponseDto cho trường hợp lỗi
            ResponseDto errorResponseDto = new ResponseDto.Builder()
                    .setStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .setMessage("Registration failed: " + e.getMessage())
                    .setData(null)
                    .build();

            // Trả về ResponseEntity với lỗi
            return new ResponseEntity<>(errorResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Password gửi lên là chuỗi đã được mã hóa
     */
    @PostMapping("/secure-register")
    @RateLimiter(name = "perUserRateLimiter", fallbackMethod = "userFallback")
    // @RateLimiter(name = "globalRateLimiter", fallbackMethod = "globalFallback")
    public ResultObject<UserDto> secureRegister(@Valid @RequestBody RegisterDto registerDto) {
        StringBuilder sb = new StringBuilder();
        try {
            // Tạo ObjectMapper để chuyển RegisterDto thành chuỗi JSON
            ObjectMapper objectMapper = new ObjectMapper();
            String registerDtoJson = objectMapper.writeValueAsString(registerDto);

            // Thêm vào StringBuilder
            sb.append(String.format("New user register at %s: \n", LocalDateTime.now()))
                    .append(registerDtoJson)
                    .append("\n");

            // System.out.println("RegisterDto: " + registerDto);
            String key = environment.getProperty("app.decrypt-key");
            Result result = DecryptUtil.decryptString(key, registerDto.password);
            if (result.isSuccess() == false) {
                throw new Exception(result.getMessage());
            }

            registerDto.password = result.getMessage();
            ResultObject<UserDto> response = authService.registerUser(registerDto);

            if (response.isSuccess == false) {
                return new ResultObject<>(false,
                        "Register failed: " + response.message,
                        response.httpStatus,
                        null);
            }

            sb.append("\r\tRegister successfully");

            _logService.logInfo(sb.toString());

            return new ResultObject<>(true,
                    "Register successfully",
                    HttpStatus.CREATED,
                    response.data);
        } catch (Exception e) { // sẽ fix lại chỗ try catch này
            e.printStackTrace();

            // Ghi log lỗi
            _logService.logError("Registration error", e);

            return new ResultObject<>(false,
                    "Register failed: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null);
        }
    }

    // Fallback cho giới hạn mỗi người dùng
    public ResultObject<UserDto> userFallback(RegisterDto registerDto, Throwable t) {
        return new ResultObject<UserDto>(false,
                "You've exceeded your request limit. Please try again later.",
                HttpStatus.TOO_MANY_REQUESTS,
                null);
    }

    public ResultObject<UserDto> globalFallback(RegisterDto registerDto, Throwable t) {
        return new ResultObject<UserDto>(false,
                "System is busy due to high traffic. Please try again later.",
                HttpStatus.TOO_MANY_REQUESTS,
                null);
    }
}