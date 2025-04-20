package com.example.Job.models.dtos;

import com.example.Job.constant.IndustryEnum;
import com.example.Job.constant.JobTypeEnum;
import com.example.Job.constant.LevelEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class JobFilter {
    private String keyword;
    private List<String> cities;
    private IndustryEnum industry;
    private JobTypeEnum jobType;
    private LevelEnum level;

    private Integer minExperience;
    private Integer maxExperience;

    private Double minSalary;
    private Double maxSalary;

}
