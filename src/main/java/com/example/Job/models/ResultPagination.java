package com.example.Job.models;

import lombok.Builder;
import org.springframework.http.HttpStatus;
import java.util.List;

@Builder
public class ResultPagination<T> {
    public boolean isSuccess;
    public String message;
    public HttpStatus httpStatus;
    public int currentPage;
    public int pageSize;
    public Integer nextPage;
    public Integer previousPage;
    public Integer totalPage;
    public Long totalElement;
    public List<T> data;


    public ResultPagination(boolean isSuccess, String message, HttpStatus httpStatus,
                            int currentPage, int pageSize, Integer nextPage,
                            Integer previousPage, Integer totalPage, Long totalElement,  List<T> data) {
        this.isSuccess = isSuccess;
        this.message = message;
        this.httpStatus = httpStatus;
        this.data = data;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.nextPage = nextPage;
        this.previousPage = previousPage;
        this.totalPage = totalPage;
        this.totalElement = totalElement;
    }
    public ResultPagination(boolean isSuccess, String message, HttpStatus httpStatus,
                            int currentPage, int pageSize, Integer nextPage,
                            Integer previousPage,  List<T> data) {
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