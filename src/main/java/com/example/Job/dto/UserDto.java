package com.example.Job.dto;

import com.example.Job.constant.GenderEnum;
import com.example.Job.constant.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private long id;

    private String email;

    private String name;

    private GenderEnum gender;

    private Date birthday;

    private RoleEnum role;



}
