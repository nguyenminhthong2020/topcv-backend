package com.example.Job.entity;

import com.example.Job.constant.IndustryEnum;
import com.example.Job.constant.RoleEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name =  "companies")
//@PrimaryKeyJoinColumn(name = "company_id")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Company extends Account {


//    private String name;

    private String overview;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    private IndustryEnum industry;

    private String companyWebsite;

    private String country;

    private String imgUrl;

    private String companySize;

    @JsonIgnore
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    private List<Job> jobs = new ArrayList<>();
    // Default no-args constructor
//    public Company() {
//        super();
//        this.role = RoleEnum.COMPANY;
//    }

    // Constructor with fields
    public Company(String email, String password, String name, String overview,
                   Address address, IndustryEnum industry, String companyWebsite,
                   String country, String imgUrl, String companySize) {
        super(email, password, RoleEnum.COMPANY);
        this.name = name;
        this.overview = overview;
        this.address = address;
        this.industry = industry;
        this.companyWebsite = companyWebsite;
        this.country = country;
        this.imgUrl = imgUrl;
        this.companySize = companySize;
        this.role = RoleEnum.COMPANY;
    }
}
