package com.example.Job.controller;

import com.example.Job.models.ResultObject;
import com.example.Job.models.dtos.JobApplyRequest;
import com.example.Job.models.dtos.RegisterDto;
import com.example.Job.models.dtos.ResponseDto;
import com.example.Job.models.dtos.UserDto;
import com.example.Job.service.Impl.ApplyService;
import com.example.Job.service.interfaces.IApplyService;
import com.example.Job.service.interfaces.ILogService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@RequestMapping("/api/v1/apply")
@RestController
public class ResumeController {

    private IApplyService applyService;
    private ILogService _logService;

    public ResumeController(IApplyService applyService, ILogService _logService) {
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
//            // Tạo ObjectMapper để chuyển RegisterDto thành chuỗi JSON
//            ObjectMapper objectMapper = new ObjectMapper();
//            String registerDtoJson = objectMapper.writeValueAsString(registerDto);
//
//            // Thêm vào StringBuilder
//            sb.append(String.format("New user register at %s: \n", LocalDateTime.now()))
//                    .append(registerDtoJson)
//                    .append("\n");

            // System.out.println("RegisterDto: " + registerDto);
            JobApplyRequest jobApplyRequest = new JobApplyRequest(email, resumeFile, coverLetter, userId, jobId);

            Object fileUrl = applyService.applyJob(jobApplyRequest);

//            if (response.isSuccess == false) {
//                ResponseDto errorResponseDto = new ResponseDto.Builder()
//                        .setStatus(response.httpStatus)
//                        .setMessage("Registration failed: " + response.message)
//                        .setData(null)
//                        .build();
//
//                // Trả về ResponseEntity với lỗi
//                return new ResponseEntity<>(errorResponseDto, response.httpStatus);
//            }

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
            _logService.logError("Registration error", e);

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

}
