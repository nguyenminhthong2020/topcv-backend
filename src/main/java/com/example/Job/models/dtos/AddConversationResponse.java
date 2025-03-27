package com.example.Job.models.dtos;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddConversationResponse {
    private long id;
    private String user1_name;  // First participant
    private String user2_name;// Second participant

    private String lastMessage;
    private Instant lastUpdated;
}
