package com.example.Job.models.dtos;

import jakarta.validation.constraints.NotNull;
import jdk.jfr.StackTrace;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyFollowRequest {

    @NotNull(message = "Company id must not be null")
    private long companyId;
}
