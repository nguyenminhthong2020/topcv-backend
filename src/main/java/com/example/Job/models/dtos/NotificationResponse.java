package com.example.Job.models.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
public class NotificationResponse {
    private Long id;

    private String message;

    private String title;

    private String senderName;

    private String recipientId;

    private boolean isRead;

    private String link;

    private Instant createdAt;

}
