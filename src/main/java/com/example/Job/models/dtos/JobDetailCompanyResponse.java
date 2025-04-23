package com.example.Job.models.dtos;

import com.example.Job.constant.*;
import com.example.Job.entity.Address;
import jakarta.persistence.GeneratedValue;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class JobDetailCompanyResponse {
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

    private boolean isSaved;

//    private boolean isApplied;

    private Instant appliedAt;

    private Instant createdAt;

    private Instant updatedAt;

    private Long companyId;

    private String companyName;

    private String companyImgUrl;

    private String companySize;

    private IndustryEnum companyIndustry;

    private Address companyAddress;
}
