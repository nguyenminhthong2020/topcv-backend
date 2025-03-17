package com.example.Job.entity;

import com.example.Job.constant.ApplyStatusEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class JobApply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String email;

//    private String phoneNumber;

    private String resume; // fileUrl

    private String coverLetter;

    @Enumerated(EnumType.STRING)
    private ApplyStatusEnum applyStatus;

    private Instant applyDate;

    @ManyToOne
    @JoinColumn(name = "applicant_id")
    private User applicant;

    @ManyToOne
    @JoinColumn(name = "job_id")
    private Job job;

    @PrePersist
    private void beforeCreate(){
        this.applyDate = Instant.now();
    }
}
