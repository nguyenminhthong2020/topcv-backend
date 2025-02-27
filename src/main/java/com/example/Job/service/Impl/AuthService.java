package com.example.Job.service.Impl;

import com.example.Job.models.ResultObject;
import com.example.Job.models.dtos.LoginDto;
import com.example.Job.models.dtos.RegisterDto;
import com.example.Job.models.dtos.UserDto;
import com.example.Job.entity.User;
import com.example.Job.repository.UserRepository;
import com.example.Job.security.JwtTokenProvider;
import com.example.Job.service.interfaces.IAuthService;

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
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    public AuthService(UserRepository userRepository, AuthenticationManager authenticationManager,
            ModelMapper modelMapper, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String login(LoginDto loginDto, User user) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                loginDto.getUsernameOrEmail(), loginDto.getPassword());
        Authentication authentication = authenticationManager.authenticate(token);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwtToken = jwtTokenProvider.createAccessToken(user);

        return jwtToken;

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultObject<UserDto> register(RegisterDto registerDto) {
        // add check for email exists in database
        if (userRepository.existsByEmail(registerDto.getEmail())) {
            // throw new RuntimeException("Email is already exist");

            // Rollback thủ công
            // ở đây tạm không cần vì chưa có thao tác update
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();

            return new ResultObject<UserDto>(false, "Email is already exist", HttpStatus.BAD_REQUEST, null);
        }

        User user = new User();
        user.setEmail(registerDto.getEmail());
        user.setName(registerDto.getName());
        user.setBirthday(registerDto.getBirthday());
        user.setGender(registerDto.getGender());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setRole(registerDto.getRole());

        // Role userRole = roleRepository.findByName("USER").get();
        // user.getRoles().add(userRole);

        User addedUser = userRepository.save(user);

        return new ResultObject<UserDto>(true, null, HttpStatus.BAD_REQUEST, modelMapper.map(addedUser, UserDto.class));
    }

}
