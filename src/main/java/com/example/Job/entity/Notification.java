package com.example.Job.entity;

import com.example.Job.security.JwtUtil;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String message;

//    private String senderName;

    private Long recipientId;

    private boolean isRead;

    private String link;

    private Instant createdAt;

    @PrePersist
    public void handleBeforeCreate() {
        this.setRead(false);

        if(this.createdAt == null) this.setCreatedAt(Instant.now());
    }
}
