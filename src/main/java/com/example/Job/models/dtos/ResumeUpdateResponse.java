package com.example.Job.models.dtos;

import com.example.Job.constant.ApplyStatusEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResumeUpdateResponse {
    private long id;

    private String resume; // fileUrl

    private ApplyStatusEnum applyStatus;

    private Instant createdAt;

    private Instant updatedAt;

    private String updatedBy;
}
