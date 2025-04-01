package com.example.Job.models.dtos;

import com.example.Job.constant.ApplyStatusEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResumeUpdateRequest {

    @NotNull(message = "Resume Id must not be null")
    private long resumeId;

    private ApplyStatusEnum applyStatus;
}
