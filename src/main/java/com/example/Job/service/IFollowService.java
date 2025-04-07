package com.example.Job.service;

public interface IFollowService {
    void followNewCompany(long companyId);

    void unFollowCompany(long companyId);

    boolean checkIfFollowed(long companyId);
}
