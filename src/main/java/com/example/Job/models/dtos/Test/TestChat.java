package com.example.Job.models.dtos.Test;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "testchat") // Tên collection trong MongoDB/ Lombok: tự động tạo getter, setter, toString //
                                   // // v.v.
public class TestChat {
    @Id
    public String id; // ID duy nhất, MongoDB sẽ tự sinh nếu không cung cấp
    public String sender;
    public String receiver;
    public String message;
    public LocalDateTime timestamp;
}
