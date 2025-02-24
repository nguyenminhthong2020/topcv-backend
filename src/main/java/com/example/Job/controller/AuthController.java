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

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private AuthService authService;
    private UserService userService;
    private JwtTokenProvider jwtTokenProvider;

    @Value("${app.jwt.refresh-token-expiration-seconds}")
    private long refreshTokenExpiration;

    @Autowired
    public AuthController(AuthService authService, UserService userService, JwtTokenProvider jwtTokenProvider) {
        this.authService = authService;
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> login(@Valid @RequestBody LoginDto loginDto) {


        User user = userService.getUserByEmail(loginDto.getUsernameOrEmail());
        JwtAuthResponse.UserLogin userLogin = new JwtAuthResponse.UserLogin(
                user.getId(), user.getEmail(), user.getName()
        );

        String accessToken = authService.login(loginDto, user);

        JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();
        jwtAuthResponse.setAccessToken(accessToken);
        jwtAuthResponse.setUserLogin(userLogin);

        String refreshToken = jwtTokenProvider.createRefreshToken(loginDto.getUsernameOrEmail(), jwtAuthResponse);

        userService.updateUserToken(refreshToken, loginDto.getUsernameOrEmail());

//        ResponseCookie responseCookie = ResponseCookie
//                .from("refreshToken", refreshToken)
//                .httpOnly(true)
//                .path("/")
//                .maxAge(refreshTokenExpiration)
//                .build();
        return ResponseEntity.ok()
//                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .body(jwtAuthResponse);


    }

    @PostMapping("/register")
    public ResponseEntity<ResponseDto> register(@Valid @RequestBody RegisterDto registerDto) {
        System.out.println("RegisterDto: " + registerDto);
        UserDto response = authService.register(registerDto);

        ResponseDto responseDto = new ResponseDto.Builder()
                .setStatus(HttpStatus.CREATED)
                .setMessage("Register successfully")
                .setData(response)
                .build();
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }
}