package com.example.Job.models.dtos;

import lombok.*;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetJobResponse {
    private long id;

    private String companyName;

    private String companyImg;

    private Long companyId;

    private String name;

    private float yearOfExperience;

    private double salaryFrom;

    private double salaryTo;

    private List<String> city;

    private Instant createdAt;

    private Instant updatedAt;


}
