package com.example.Job.models.dtos;

import java.util.List;

public interface GetJobResponseDto {
    Long getId();
    String getCompanyName();
    String getCompanyImg();
    Long getCompanyId();
    String getName();
    Integer getYearOfExperience();
    Double getSalaryFrom();
    Double getSalaryTo();
    List<String> getCity();  // Will be converted automatically
}
