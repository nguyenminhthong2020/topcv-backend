package com.example.Job.models.dtos;

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

    private String name;

    private String location;

    private double salaryFrom;

    private double salaryTo;

    private int quantity; // NUMBER OF APPLICANT

    @Enumerated(EnumType.STRING)
    private JobTypeEnum jobType;

    private LevelEnum level; // INTERN, FRESHER, SENIOR

    private int yearOfExperience;

    private String description;

    private Instant startDate;
    private Instant endDate;

    private boolean active;

    private long companyId;

    private List<Skill> skills;

}
