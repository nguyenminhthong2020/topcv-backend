package com.example.Job.models.dtos;

import com.example.Job.constant.JobStatusEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;


@Getter
@Setter
@NoArgsConstructor
public class JobPostResponse {
    private long id;

    private String name;

    private JobStatusEnum status;

    private Instant deadline;

    private Instant createdAt;


}
