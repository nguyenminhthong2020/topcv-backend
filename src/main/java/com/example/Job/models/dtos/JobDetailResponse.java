package com.example.Job.models.dtos;

import com.example.Job.constant.*;
import com.example.Job.entity.Company;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class JobDetailResponse {

    private long id;

    private String name;

    private List<String> city;

    private List<String> skills;

    private double salaryFrom;

    private double salaryTo;

    private int quantity; // NUMBER OF APPLICANT

    private IndustryEnum industry;

    private GenderRequireEnum genderRequire;

    private JobTypeEnum jobType; // FULL_TIME, PART_TIME, REMOTE, HYBRID

    private LevelEnum level; // INTERN, FRESHER, SENIOR

    private EducationLevelEnum educationLevel;

    private float yearOfExperience;

    private String description;

    private String detail;

    private Instant deadline;

    //    private boolean active;

    private JobStatusEnum jobStatus;

    private Instant createdAt;

    private Instant updatedAt;
}
