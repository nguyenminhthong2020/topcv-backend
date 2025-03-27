package com.example.Job.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JobApplyRequest {

    private String email;

    private MultipartFile resume; // fileUrl

    private String coverLetter;

    private Long userId;

    private Long jobId;
}

