package com.example.Job.models.dtos;

import com.example.Job.constant.IndustryEnum;
import com.example.Job.entity.Address;
import jakarta.persistence.Embedded;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CompanyRegister {

    @Email(message = "Email must be valid")
    @NotBlank(message = "Email must not be blank")
    private String email;

    @NotBlank(message = "Password must not be blank")
    public String password;

    @NotBlank(message = "Company name must not be blank")
    private String name;

    private String overview;

    private String province;

    private String district;

    private String location;

    private IndustryEnum industry;

    private String companyWebsite;

    private String country;

    private String imgUrl;

    private String companySize;
}
