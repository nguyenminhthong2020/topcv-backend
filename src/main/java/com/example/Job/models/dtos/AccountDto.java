package com.example.Job.models.dtos;

import com.example.Job.constant.GenderEnum;
import com.example.Job.constant.RoleEnum;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountDto {
    private long id;

    private String email;

    private String name;

    private RoleEnum role;

    public AccountDto(long id, String name, RoleEnum role) {
        this.id = id;
        this.name = name;
        this.role = role;
    }
}
