package com.example.Job.controller;

import com.example.Job.models.dtos.JobDto;
import com.example.Job.models.dtos.ResponseDto;
import com.example.Job.service.interfaces.IJobService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/jobs")
public class JobController {
    private IJobService jobService;

    public JobController(IJobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping
    public ResponseEntity<ResponseDto> createJob(@Valid @RequestBody JobDto jobDto) {


        JobDto addJob = jobService.createJob(jobDto);

        ResponseDto responseDto = new ResponseDto.Builder()
                .setStatus(HttpStatus.CREATED)
                .setMessage("Create new Job successfully")
                .setData(addJob)
                .build();
        return new ResponseEntity(responseDto, HttpStatus.CREATED);

    }


}