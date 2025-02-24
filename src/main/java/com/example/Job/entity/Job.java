package com.example.Job.entity;

import com.example.Job.constant.LevelEnum;
import com.example.Job.security.JwtTokenProvider;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "jobs")
@Getter
@Setter
@NoArgsConstructor
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    private String location;
    private double salary;
    private int quantity;
    private LevelEnum level;

    @Column(columnDefinition = "TEXT")
    private String description;

    private Instant startDate;
    private Instant endDate;

    private boolean active;

    private Instant createdAt;

    private Instant updatedAt;

    private String createdBy;

    private String updatedBy;


    // Before add a new job, set CreatedBy to email who add the job
    @PrePersist
    public void handleBeforeCreate() {

        this.setCreatedBy(JwtTokenProvider.getCurrentUserLogin()
                .orElseThrow(() -> null));
        this.setCreatedAt(Instant.now());
    }

    @PreUpdate
    public void handleBeforeUpdate() {

        this.setUpdatedBy(JwtTokenProvider.getCurrentUserLogin()
                .orElseThrow(() -> null));
        this.setUpdatedAt(Instant.now());
    }
}