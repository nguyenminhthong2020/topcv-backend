package com.example.Job.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoginDto {

    @NotBlank(message = "Email must not be blank")
    @Email(message = "Must be valid email")
    @JsonProperty("email")
    private String usernameOrEmail;

    @NotBlank(message = "Password must not be blank")
    private String password;


}