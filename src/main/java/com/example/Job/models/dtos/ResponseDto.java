package com.example.Job.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class ResponseDto {
    private HttpStatus status;
    private String message;
    private Object data;
    private boolean isSuccess;

    public ResponseDto() {
    };

    private ResponseDto(HttpStatus status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }



    public static class Builder {
        private HttpStatus status;
        private String message;
        private Object data;

        public Builder() {
        }

        public ResponseDto build() {
            return new ResponseDto(this.status, this.message, this.data);
        }

        public Builder setStatus(HttpStatus status) {
            this.status = status;
            return this;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setData(Object data) {
            this.data = data;
            return this;
        }
    }

}
