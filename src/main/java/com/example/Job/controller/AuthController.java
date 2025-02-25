package com.example.Job.controller;

import com.example.Job.dto.LoginDto;
import com.example.Job.dto.RegisterDto;
import com.example.Job.dto.ResponseDto;
import com.example.Job.dto.UserDto;
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

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private AuthService authService;
    private UserService userService;
    private JwtTokenProvider jwtTokenProvider;

    private ILogService _logService;

    @Value("${app.jwt.refresh-token-expiration-seconds}")
    private long refreshTokenExpiration;

    @Autowired
    public AuthController(AuthService authService, UserService userService,
            JwtTokenProvider jwtTokenProvider, ILogService logServivce) {
        this.authService = authService;
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
        this._logService = logServivce;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> login(@Valid @RequestBody LoginDto loginDto) {

        User user = userService.getUserByEmail(loginDto.getUsernameOrEmail());
        JwtAuthResponse.UserLogin userLogin = new JwtAuthResponse.UserLogin(
                user.getId(), user.getEmail(), user.getName());

        String accessToken = authService.login(loginDto, user);

        JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();
        jwtAuthResponse.setAccessToken(accessToken);
        jwtAuthResponse.setUserLogin(userLogin);

        String refreshToken = jwtTokenProvider.createRefreshToken(loginDto.getUsernameOrEmail(), jwtAuthResponse);

        userService.updateUserToken(refreshToken, loginDto.getUsernameOrEmail());

        // ResponseCookie responseCookie = ResponseCookie
        // .from("refreshToken", refreshToken)
        // .httpOnly(true)
        // .path("/")
        // .maxAge(refreshTokenExpiration)
        // .build();
        return ResponseEntity.ok()
                // .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .body(jwtAuthResponse);

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
            UserDto response = authService.register(registerDto);

            ResponseDto responseDto = new ResponseDto.Builder()
                    .setStatus(HttpStatus.CREATED)
                    .setMessage("Register successfully")
                    .setData(response)
                    .build();

            sb.append("\r\tRegister successfully");

            _logService.logInfo(sb.toString());

            return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
        } catch (Exception e) { // sẽ fix lại chỗ try catch này
            e.printStackTrace();

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
}