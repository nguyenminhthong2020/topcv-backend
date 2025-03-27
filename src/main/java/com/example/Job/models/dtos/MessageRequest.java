package com.example.Job.models.dtos;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class MessageRequest {
    private long id;

    @NotBlank(message = "Message must not be null")
    private String content;

    @NotBlank(message = "Sender ID must not be null")
    private String senderId;
    @NotBlank(message = "Receiver ID must not be null")
    private String recipientId;
    private String senderName;
    private String senderRole;
    private Instant timeStamp;

    @NotNull(message = "Conversation Id must not be null")
    private Long conversationId;
}
