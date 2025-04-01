package com.example.Job.models.dtos;

import com.example.Job.constant.JobStatusEnum;
import com.example.Job.constant.JobTypeEnum;
import com.example.Job.constant.LevelEnum;
import com.example.Job.entity.Skill;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
public class JobDto {

    private long id;

    private String name;

    private String[] city;

//    private String district;
    private double salaryFrom;

    private double salaryTo;

    private int quantity; // NUMBER OF APPLICANT

    @Enumerated(EnumType.STRING)
    private JobTypeEnum jobType;

    private LevelEnum level; // INTERN, FRESHER, SENIOR

    private int yearOfExperience;

    private String description;

    private Instant deadline;

    private JobStatusEnum jobStatus;

    private long companyId;

    private List<String> skills;

    private Instant createdAt;

}
