package com.example.Job.entity;

import com.example.Job.constant.*;
import com.example.Job.security.JwtUtil;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "jobs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    @Column(columnDefinition = "TEXT[]")
    private List<String> city;

    @Column(columnDefinition = "TEXT[]")
    private List<String> skills;

    private double salaryFrom;

    private double salaryTo;

    private int quantity; // NUMBER OF APPLICANT

    @Enumerated(EnumType.STRING)
    private IndustryEnum industry;

    @Enumerated(EnumType.STRING)
    private JobTypeEnum jobType; // FULL_TIME, PART_TIME, REMOTE, HYBRID

    @Enumerated(EnumType.STRING)
    private LevelEnum level; // INTERN, FRESHER, SENIOR

    @Enumerated(EnumType.STRING)
    private EducationLevelEnum educationLevel;

    private int yearOfExperience;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String detail;

    private Instant deadline;

//    private boolean active;
    @Enumerated(EnumType.STRING)
    private JobStatusEnum jobStatus;

    @Column(name = "search_vector", columnDefinition = "tsvector",  insertable = false, updatable = false )
    private String searchVector;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    private Instant createdAt;

    private Instant updatedAt;

    private String createdBy;

    private String updatedBy;

    @OneToMany(mappedBy = "job")
    private Set<JobApply> jobApplies = new HashSet<>();

//    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    @JoinTable(name = "job_skill",
//            joinColumns = @JoinColumn(name = "job_id"),
//            inverseJoinColumns = @JoinColumn(name = "skill_id")
//    )
//    private Set<Skill> skills = new HashSet<>();

    // Before add a new job, set CreatedBy to email who add the job
    @PrePersist
    public void handleBeforeCreate() {

        this.setCreatedBy(JwtUtil.getCurrentUserLogin()
                .orElseThrow(() -> null));
        this.setCreatedAt(Instant.now());
    }

    @PreUpdate
    public void handleBeforeUpdate() {

        this.setUpdatedBy(JwtUtil.getCurrentUserLogin()
                .orElseThrow(() -> null));
        this.setUpdatedAt(Instant.now());
    }
}