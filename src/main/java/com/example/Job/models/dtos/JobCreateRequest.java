package com.example.Job.models.dtos;

import com.example.Job.constant.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.TimeZone;

@Getter
@Setter
public class JobCreateRequest {

    @NotBlank(message = "Job name must not be blank")
    private String name;

    @Size(min = 0, message = "City list must greater than 0 ")
    @NotNull(message = "Location must not be null")
    private List<String> city;

    private List<String> skills;

    @NotNull(message = "Min salary must not be null")
    @Positive(message = "Salary must be greater than 0")
    private Double salaryFrom;

    @Positive(message = "Max salary must be positive")
    private Double salaryTo;

    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity; // Number of applicants

    private GenderRequireEnum genderRequire;

    private JobTypeEnum jobType;

    private LevelEnum level; // INTERN, FRESHER, SENIOR

    private EducationLevelEnum educationLevel;

    private IndustryEnum industry;

    @Min(value = 0, message = "Years of experience must be at least 0")
    private float yearOfExperience;

    @NotBlank(message = "Description must not be blank")
    @Size(max = 5000, message = "Description must be at most 5000 characters")
    private String description;

    private String detail;



    private LocalDate deadline;

    private JobStatusEnum jobStatus;

    @NotNull(message = "Company ID must not be null")
    @Positive(message = "Company ID must be a positive number")
    private Long companyId;

}
