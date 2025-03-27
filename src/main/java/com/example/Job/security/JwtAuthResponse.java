package com.example.Job.security;

import com.example.Job.constant.RoleEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

// dto class for jwt login response
public class JwtAuthResponse
{
    @JsonProperty("accessToken")
    private String accessToken;

    @JsonProperty("user")
    private UserLogin userLogin;

    public JwtAuthResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    public JwtAuthResponse(){};


    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public UserLogin getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(UserLogin userLogin) {
        this.userLogin = userLogin;
    }

    @Getter
    @Setter
    public static class UserLogin{
        private long id;
        private String email;
        private String name;
        private RoleEnum role;

        public UserLogin(long id, String email, String name, RoleEnum role) {
            this.id = id;
            this.email = email;
            this.name = name;
            this.role = role;
        }


    }


}
