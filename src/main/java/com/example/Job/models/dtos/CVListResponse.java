package com.example.Job.models.dtos;

import com.example.Job.constant.ApplyStatusEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class CVListResponse {
    private long id;

    private String resume; // fileUrl

    private String email;

    @Enumerated(EnumType.STRING)
    private ApplyStatusEnum applyStatus;

    private Instant createdAt;

    private Instant updatedAt;

    private String updatedBy;

    private Long jobId;

    private String jobName;

    private String userName;

    private String coverLetter;
}
