package com.example.Job.security;

import com.fasterxml.jackson.annotation.JsonProperty;

// dto class for jwt login response
public class JwtAuthResponse
{
    @JsonProperty("accessToken")
    private String accessToken;

    @JsonProperty("user")
    UserLogin user;

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
        return user;
    }

    public void setUserLogin(UserLogin userLogin) {
        this.user = userLogin;
    }

    public static class UserLogin{
        private long id;
        private String email;
        private String name;
        private String role;

        public UserLogin(long id, String email, String name) {
            this.id = id;
            this.email = email;
            this.name = name;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }
    }


}
