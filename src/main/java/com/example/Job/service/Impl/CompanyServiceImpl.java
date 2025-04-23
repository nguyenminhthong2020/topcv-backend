package com.example.Job.service.Impl;

import com.example.Job.config.RedisConfig;
import com.example.Job.constant.RoleEnum;
import com.example.Job.entity.Address;
import com.example.Job.entity.Company;
import com.example.Job.entity.CompanyFollow;
import com.example.Job.entity.Id.CompanyFollowId;
import com.example.Job.entity.User;
import com.example.Job.exception.ResourceNotFoundException;
import com.example.Job.models.dtos.CompanyRegister;
import com.example.Job.models.dtos.CompanyDetailResponse;
import com.example.Job.repository.AccountRepository;
import com.example.Job.repository.CompanyRepository;
import com.example.Job.security.JwtUtil;
import com.example.Job.service.IAccountService;
import com.example.Job.service.ICompanyService;
import com.example.Job.service.IRedisService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class CompanyServiceImpl implements ICompanyService {

    private static final Logger log = LoggerFactory.getLogger(CompanyServiceImpl.class);
    private final CompanyRepository companyRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final IRedisService redisService;
    private final JwtUtil jwtUtil;
    private final IAccountService accountService;

    public CompanyServiceImpl(CompanyRepository companyRepository, AccountRepository accountRepository, PasswordEncoder passwordEncoder, ModelMapper modelMapper, IRedisService redisService, JwtUtil jwtUtil, IAccountService accountService) {
        this.companyRepository = companyRepository;
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
        this.redisService = redisService;
        this.jwtUtil = jwtUtil;
        this.accountService = accountService;
    }

    @Override
    public Company registerCompany(CompanyRegister registerRequest) {

        if (accountRepository.existsByEmail(registerRequest.getEmail())){
            throw new RuntimeException("Email already exists");
        }

        Company company = Company.builder()
                .industry(registerRequest.getIndustry())
                .companySize(registerRequest.getCompanySize())
                .companyWebsite(registerRequest.getCompanyWebsite())
                .overview(registerRequest.getOverview())
                .address(new Address(registerRequest.getProvince(), registerRequest.getDistrict(), registerRequest.getLocation()))
                .country(registerRequest.getCountry())
                .imgUrl(registerRequest.getImgUrl())
                .build();

        company.setEmail(registerRequest.getEmail());
        company.setPassword(passwordEncoder.encode( registerRequest.getPassword()));
        company.setRole(RoleEnum.COMPANY);
        company.setName(registerRequest.getName());

        try {
            Company savedCompany = companyRepository.save(company);

            return savedCompany;
        } catch (Exception e) {
            log.error( e.getMessage());
            throw new RuntimeException("Failed to register company. Please try again.");
        }

    }


    @Override
    public CompanyDetailResponse getCompanyDetailById(Long companyId) {

        String key = RedisConfig.generateKey(CompanyDetailResponse.class, "id", companyId);
        CompanyDetailResponse cacheCompany = redisService.get(key, CompanyDetailResponse.class);

        // If find in cache, return the cached Data
        if(cacheCompany != null){
            return cacheCompany;
        }

        // If not found in cache, get from DB and cache the data
        Company company = getCompanyById(companyId);

        CompanyDetailResponse companyDetailResponse = modelMapper.map(company, CompanyDetailResponse.class);
        companyDetailResponse.setNumOfFollowers( company.getCompanyFollowers().size() );

        redisService.set(key, companyDetailResponse, Duration.ofHours(24));

        return companyDetailResponse;


    }

    @Override
    public Company getCompanyById(Long id) {


        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company", "id", id));

        return company;
    }

//    @Override
//    public void followNewCompany(long companyId) {
//        String userId = jwtUtil.extractUserIdFromToken();
//        CompanyFollowId companyFollowId = new CompanyFollowId(companyId, Long.parseLong(userId));
//        User user = new User();
//        user.setId(Long.parseLong(userId));
//
//        Company company = new Company();
//        company.setId(companyId);
//
//        CompanyFollow companyFollow = new CompanyFollow(companyFollowId, user,company);
//
//
//
//    }

}
