package com.example.Job.models.dtos;

import com.example.Job.constant.JobStatusEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateJobStatusRequest {
    @NotNull(message = "Job id must not be null")
    private long jobId;

    private JobStatusEnum jobStatus;
}
