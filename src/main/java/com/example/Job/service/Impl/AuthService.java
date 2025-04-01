package com.example.Job.service.Impl;

import com.example.Job.constant.RoleEnum;
import com.example.Job.entity.Account;
import com.example.Job.models.ResultObject;
import com.example.Job.models.dtos.LoginRequest;
import com.example.Job.models.dtos.RegisterRequest;
import com.example.Job.models.dtos.UserDto;
import com.example.Job.entity.User;
import com.example.Job.repository.UserRepository;
import com.example.Job.security.JwtUtil;
import com.example.Job.service.IAuthService;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

@Service
public class AuthService implements IAuthService {
    private UserRepository userRepository;
    private AuthenticationManager authenticationManager;
    private ModelMapper modelMapper;
    private PasswordEncoder passwordEncoder;
    private JwtUtil jwtUtil;

    @Autowired
    public AuthService(UserRepository userRepository, AuthenticationManager authenticationManager,
            ModelMapper modelMapper, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String login(LoginRequest loginRequest, Account user) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                loginRequest.getUsernameOrEmail(), loginRequest.getPassword());
        Authentication authentication = authenticationManager.authenticate(token);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwtToken = jwtUtil.createAccessToken(user);

        return jwtToken;

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultObject<UserDto> registerUser(RegisterRequest registerRequest) {
        // add check for email exists in database
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            // throw new RuntimeException("Email is already exist");

            // Rollback thủ công
            // ở đây tạm không cần vì chưa có thao tác update
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();

            return new ResultObject<UserDto>(false, "Email is already exist", HttpStatus.BAD_REQUEST, null);
        }

        User user = new User();
        user.setEmail(registerRequest.getEmail());
        user.setName(registerRequest.getName());
        user.setBirthday(registerRequest.getBirthday());
        user.setGender(registerRequest.getGender());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        user.setRole( registerRequest.getRole() != null ? registerRequest.getRole() : RoleEnum.USER);

        // Role userRole = roleRepository.findByName("USER").get();
        // user.getRoles().add(userRole);

        User addedUser = userRepository.save(user);

        return new ResultObject<UserDto>(true, null, HttpStatus.CREATED, modelMapper.map(addedUser, UserDto.class));
    }

}
