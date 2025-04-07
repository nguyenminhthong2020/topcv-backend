package com.example.Job.controller;

import com.example.Job.entity.Company;
import com.example.Job.models.ResultObject;
import com.example.Job.models.dtos.CheckFollowResponse;
import com.example.Job.models.dtos.CompanyFollowRequest;
import com.example.Job.models.dtos.CompanyRegister;
import com.example.Job.service.IFollowService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/follow")
public class CompanyFollowController {

    private final IFollowService followService;

    public CompanyFollowController(IFollowService followService) {
        this.followService = followService;
    }


    @PostMapping("/company")
    public ResponseEntity<ResultObject> followCompany(@Valid @RequestBody CompanyFollowRequest followRequest) {


        followService.followNewCompany(followRequest.getCompanyId());

        ResultObject<Void> result = new ResultObject<>(true, "Follow successfully", HttpStatus.OK, null);

        return new ResponseEntity<>(result, HttpStatus.OK);

    }

    @PutMapping("/company")
    public ResponseEntity<ResultObject> unFollowCompany(@Valid @RequestBody CompanyFollowRequest unFollowRequest) {


        followService.unFollowCompany(unFollowRequest.getCompanyId());

        ResultObject<Void> result = new ResultObject<>(true, "Unfollow successfully", HttpStatus.OK, null);

        return new ResponseEntity<>(result, HttpStatus.OK);

    }

    @GetMapping("/company/check")
    public ResponseEntity<ResultObject> checkIfFollowed(@RequestParam(value = "companyId") Long companyId) {


        boolean isFollowed = followService.checkIfFollowed(companyId);

        CheckFollowResponse checkFollowResponse = new CheckFollowResponse(isFollowed);
        ResultObject<CheckFollowResponse> result = new ResultObject<>(true, "OK", HttpStatus.OK, checkFollowResponse);

        return new ResponseEntity<>(result, HttpStatus.OK);

    }
}
