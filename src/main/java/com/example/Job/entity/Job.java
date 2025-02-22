package com.example.Job.entity;

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
//    private LevelEnum level;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String description;

    private Instant startDate;
    private Instant endDate;

    private boolean active;

    private Instant createdAt;

    private Instant updatedAt;

    private String createdBy;

    private String updatedBy;


//    @PrePersist
//    public void handleBeforeCreate() {
//
//        this.setCreatedBy(JwtTokenProvider.getCurrentUserLogin()
//                .orElseThrow(() -> null));
//        this.setCreatedAt(Instant.now());
//    }
//
//    @PreUpdate
//    public void handleBeforeUpdate() {
//
//        this.setUpdatedBy(JwtTokenProvider.getCurrentUserLogin()
//                .orElseThrow(() -> null));
//        this.setUpdatedAt(Instant.now());
//    }
}