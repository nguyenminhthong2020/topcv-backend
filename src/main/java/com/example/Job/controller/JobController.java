package com.example.Job.controller;

import com.example.Job.entity.Job;
import com.example.Job.models.ResultPagination;
import com.example.Job.models.dtos.JobDto;
import com.example.Job.models.dtos.ResponseDto;
import com.example.Job.service.interfaces.IJobService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
    public ResponseEntity<ResultPagination<JobDto>> getJobsPagination(@RequestParam(value = "current", defaultValue = "1") int current,
                                                                      @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                                                      @RequestParam(value = "sortBy", defaultValue = "yearOfExperience") String sortBy,
                                                                      @RequestParam(value = "ascending", defaultValue = "true") String isAscending) {

//        int defaultPage = 1;
//        int defaultPageSize = 5;
//
//        int currentPage =current.isPresent() ? Integer.parseInt(current.get()) : defaultPage;
//        int size = pageSize.isPresent() ? Integer.parseInt( pageSize.get()) : defaultPageSize;

//        List<JobDto> jobDtos = jobService.getAllJobs(current - 1, pageSize);

        Page<JobDto> jobResPage = jobService.getAllJobs(current - 1, pageSize, sortBy, Boolean.valueOf(isAscending));

        ResultPagination<JobDto> res = ResultPagination.<JobDto>builder()
                .isSuccess(true)
                .message("Get jobs successfully")
                .httpStatus(HttpStatus.OK)
                .currentPage(jobResPage.getNumber() + 1)
                .pageSize(jobResPage.getSize())
                .previousPage(jobResPage.hasPrevious() ? jobResPage.getNumber() : null)
                .nextPage(jobResPage.hasNext() ? jobResPage.getNumber() + 2 : null )
                .data(jobResPage.getContent())
                .totalPage(jobResPage.getTotalPages())
                .build();


        return new ResponseEntity<>(res, HttpStatus.OK);
    }


}