package com.example.Job.models.dtos;

import com.example.Job.entity.Account;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddConversationRequest {

    private long user1_ID;  // First participant

    private long user2_ID;  // Second participant

}
