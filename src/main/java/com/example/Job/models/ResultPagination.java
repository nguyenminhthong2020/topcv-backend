package com.example.Job.models;

import org.springframework.http.HttpStatus;
import java.util.List;


public class ResultPagination<T> {
    public boolean isSuccess;
    public String message;
    public HttpStatus httpStatus;
    public List<T> data;
    public int currentPage;
    public int pageSize;
    public Integer nextPage;
    public Integer previousPage;

    public ResultPagination(boolean isSuccess, String message, HttpStatus httpStatus, List<T> data,
            int currentPage, int pageSize, Integer nextPage, Integer previousPage) {
        this.isSuccess = isSuccess;
        this.message = message;
        this.httpStatus = httpStatus;
        this.data = data;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.nextPage = nextPage;
        this.previousPage = previousPage;
    }
}