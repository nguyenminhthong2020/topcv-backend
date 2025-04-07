package com.example.Job.service.Impl;

import com.example.Job.entity.Company;
import com.example.Job.entity.CompanyFollow;
import com.example.Job.entity.CompanyFollowId;
import com.example.Job.entity.User;
import com.example.Job.repository.CompanyFollowRepository;
import com.example.Job.security.JwtUtil;
import com.example.Job.service.IFollowService;
import org.springframework.stereotype.Service;

@Service
public class FollowServiceImpl implements IFollowService {

    private final CompanyFollowRepository companyFollowRepository;
    private final JwtUtil jwtUtil;

    public FollowServiceImpl(CompanyFollowRepository companyFollowRepository, JwtUtil jwtUtil) {
        this.companyFollowRepository = companyFollowRepository;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void followNewCompany(long companyId) {
        String userId = jwtUtil.extractUserIdFromToken();

        CompanyFollowId companyFollowId = new CompanyFollowId(companyId, Long.parseLong(userId));
        User user = new User();
        user.setId(Long.parseLong(userId));

        Company company = new Company();
        company.setId(companyId);

        CompanyFollow companyFollow = new CompanyFollow(companyFollowId, user,company);

        try{
            companyFollowRepository.save(companyFollow);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void unFollowCompany(long companyId) {
        String userId = jwtUtil.extractUserIdFromToken();

        CompanyFollowId companyFollowId = new CompanyFollowId(companyId, Long.parseLong(userId));

        try{
            companyFollowRepository.deleteById(companyFollowId);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public boolean checkIfFollowed(long companyId) {
        String userId = jwtUtil.extractUserIdFromToken();

        CompanyFollowId companyFollowId = new CompanyFollowId(companyId, Long.parseLong(userId));

        return companyFollowRepository.existsById(companyFollowId);
    }
}
