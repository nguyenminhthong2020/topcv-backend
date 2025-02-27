package com.example.Job.models;

import org.springframework.http.HttpStatus;

public class ResultObject<T> {
    public boolean isSuccess;
    public String message;
    public HttpStatus httpStatus;
    public T data;

    public ResultObject(boolean isSuccess, String message, HttpStatus httpStatus, T data) {
        this.isSuccess = isSuccess;
        this.message = message;
        this.httpStatus = httpStatus;
        this.data = data;
    }

    public static <T> ResultObject<T> success(T data, String message) {
        return new ResultObject<>(true, message, HttpStatus.OK, data);
    }

    public static <T> ResultObject<T> error(String message, HttpStatus status) {
        return new ResultObject<>(false, message, status, null);
    }
}
