package com.example.Job.models.dtos;

import com.example.Job.constant.IndustryEnum;
import com.example.Job.entity.Address;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CompanyDetailResponse {
    private long id;

    private String name;

    private String overview;

    private Address address;

    private IndustryEnum industry;

    private String companyWebsite;

    private String country;

    private String imgUrl;

    private String companySize;

    private long numOfFollowers;

}
