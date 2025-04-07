package com.example.Job.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequest {

    private String message;

    private String title;

//    private String senderName;

//    private String recipientId;

    private String link;

    private Instant createdAt;
}
