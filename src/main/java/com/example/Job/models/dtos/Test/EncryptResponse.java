package com.example.Job.models.dtos.Test;

public class EncryptResponse {
    private String encryptText;

    // Constructor
    public EncryptResponse(String encryptText) {
        this.encryptText = encryptText;
    }

    // Getter và Setter
    public String getEncryptText() {
        return encryptText;
    }

    public void setEncryptText(String encryptText) {
        this.encryptText = encryptText;
    }
}