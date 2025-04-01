package com.example.Job.models.dtos;

import com.example.Job.constant.ApplyStatusEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.time.Instant;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetJobApplyResponse {
    private long id;

    private String resume; // fileUrl

    @Enumerated(EnumType.STRING)
    private ApplyStatusEnum applyStatus;

    private Instant createdAt;

    private Instant updatedAt;

    private String updatedBy;

    private GetJobResponse job;



}
