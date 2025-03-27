package com.example.Job.models.dtos;

import com.example.Job.entity.Account;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.Instant;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class GetConversationResponse {
    private long id;
    private String lastMessage;
    private Instant lastUpdated;

//    private Account user;

    @JsonProperty("user")
    private AccountDto otherUser;
}
