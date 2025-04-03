package com.example.Job.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name ="conversations")
@Getter
@Setter
public class Conversation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user1_id")
    private Account user1;  // First participant

    @ManyToOne
    @JoinColumn(name = "user2_id")
    private Account user2;  // Second participant

    private String lastMessage;

    @Column(name = "last_updated")
    private Instant lastUpdated;


}
