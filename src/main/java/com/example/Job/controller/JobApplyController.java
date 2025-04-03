package com.example.Job.controller;

import com.example.Job.constant.ApplyStatusEnum;
import com.example.Job.models.ResultPagination;
import com.example.Job.models.dtos.*;
import com.example.Job.service.IApplyService;
import com.example.Job.service.ILogService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/api/v1/apply")
@RestController
public class JobApplyController {

    private IApplyService applyService;
    private ILogService _logService;

    public JobApplyController(IApplyService applyService, ILogService _logService) {
        this.applyService = applyService;
        this._logService = _logService;
    }

    @PostMapping
    public ResponseEntity<ResponseDto> applyForJob( @RequestParam("email") String email,
                                                  @RequestParam("coverLetter") String coverLetter,
                                                  @RequestParam("userId") Long userId,
                                                  @RequestParam("jobId") Long jobId,
                                                  @RequestPart("resume") MultipartFile resumeFile) {
        StringBuilder sb = new StringBuilder();
        try {

            JobApplyRequest jobApplyRequest = new JobApplyRequest(email, resumeFile, coverLetter, userId, jobId);

            Object fileUrl = applyService.applyJob(jobApplyRequest);


            ResponseDto responseDto = new ResponseDto.Builder()
                    .setStatus(HttpStatus.CREATED)
                    .setMessage("Apply successfully")
                    .setData(fileUrl)
                    .build();

            sb.append("\r\tApply successfully");

            _logService.logInfo(sb.toString());

            return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
        } catch (Exception e) { // sẽ fix lại chỗ try catch này
            e.printStackTrace();

            // Ghi log lỗi
            _logService.logError("Apply error", e);

            // Tạo ResponseDto cho trường hợp lỗi
            ResponseDto errorResponseDto = new ResponseDto.Builder()
                    .setStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .setMessage("Apply failed: " + e.getMessage())
                    .setData(null)
                    .build();

            // Trả về ResponseEntity với lỗi
            return new ResponseEntity<>(errorResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping
    public ResponseEntity<ResultPagination> getJobApplyByUser(@RequestParam(value = "currentPage", defaultValue = "1") int current,
                                                            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                                            @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
                                                            @RequestParam(value = "ascending", defaultValue = "true") String isAscending) {

        Page<GetJobApplyResponse> jobResPage = applyService.getJobApplyByUser(current - 1, pageSize, sortBy, Boolean.parseBoolean(isAscending));

        ResultPagination<GetJobApplyResponse> res = ResultPagination.<GetJobApplyResponse>builder()
                .isSuccess(true)
                .message("Get jobs successfully")
                .httpStatus(HttpStatus.OK)
                .currentPage(jobResPage.getNumber() + 1)
                .pageSize(jobResPage.getSize())
                .previousPage(jobResPage.hasPrevious() ? jobResPage.getNumber() : null)
                .nextPage(jobResPage.hasNext() ? jobResPage.getNumber() + 2 : null)
                .data(jobResPage.getContent())
                .totalPage(jobResPage.getTotalPages())
                .totalElement(jobResPage.getTotalElements())
                .build();


        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/job")
    public ResponseEntity<ResultPagination> getJobApplyByJob(@RequestParam(value = "jobId") Long jobId,
                                                            @RequestParam(value = "currentPage", defaultValue = "1") int current,
                                                              @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                                              @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
                                                              @RequestParam(value = "ascending", defaultValue = "true") String isAscending,
                                                             @RequestParam(value = "applyStatus", required = false) ApplyStatusEnum applyStatus
                                                             ) {

        Page<CVListResponse> jobResPage = applyService.getApplyByJob(jobId, applyStatus,current - 1, pageSize, sortBy, Boolean.parseBoolean(isAscending));

        ResultPagination<CVListResponse> res = ResultPagination.<CVListResponse>builder()
                .isSuccess(true)
                .message("Get job apply successfully")
                .httpStatus(HttpStatus.OK)
                .currentPage(jobResPage.getNumber() + 1)
                .pageSize(jobResPage.getSize())
                .previousPage(jobResPage.hasPrevious() ? jobResPage.getNumber() : null)
                .nextPage(jobResPage.hasNext() ? jobResPage.getNumber() + 2 : null)
                .data(jobResPage.getContent())
                .totalPage(jobResPage.getTotalPages())
                .totalElement(jobResPage.getTotalElements())
                .build();


        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/company")
    public ResponseEntity<ResultPagination> getJobApplyByCompany(@RequestParam(value = "companyId") Long companyId,
                                                                 @RequestParam(value = "currentPage", defaultValue = "1") int current,
                                                                 @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                                                 @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
                                                                 @RequestParam(value = "ascending", defaultValue = "true") String isAscending,
                                                                 @RequestParam(value = "applyStatus", required = false) ApplyStatusEnum applyStatus,
                                                                 @RequestParam(value = "keyword", required = false) String keyword
                                                                 ) {

        Page<CVListResponse> jobResPage = applyService.getJobApplyByCompany(keyword, applyStatus, companyId,current - 1, pageSize, sortBy, Boolean.parseBoolean(isAscending));

        ResultPagination<CVListResponse> res = ResultPagination.<CVListResponse>builder()
                .isSuccess(true)
                .message("Get job apply successfully")
                .httpStatus(HttpStatus.OK)
                .currentPage(jobResPage.getNumber() + 1)
                .pageSize(jobResPage.getSize())
                .previousPage(jobResPage.hasPrevious() ? jobResPage.getNumber() : null)
                .nextPage(jobResPage.hasNext() ? jobResPage.getNumber() + 2 : null)
                .data(jobResPage.getContent())
                .totalPage(jobResPage.getTotalPages())
                .totalElement(jobResPage.getTotalElements())
                .build();


        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseDto> getJobApplyByCompany(@Valid @RequestBody ResumeUpdateRequest resumeUpdateRequest) {

        ResumeUpdateResponse updatedResume = applyService.updateResumeStatus(resumeUpdateRequest.getResumeId(), resumeUpdateRequest.getApplyStatus());

        ResponseDto res = new ResponseDto.Builder()
                .setStatus(HttpStatus.OK)
                .setMessage("Update successfully")
                .setData(updatedResume)
                .build();

        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
