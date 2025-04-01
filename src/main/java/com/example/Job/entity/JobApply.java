package com.example.Job.entity;

import com.example.Job.constant.ApplyStatusEnum;
import com.example.Job.security.JwtUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

    private Instant createdAt;

    private Instant updatedAt;

    private String updatedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "applicant_id")
    @JsonIgnore
    private User applicant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Job job;

    @PrePersist
    private void beforeCreate()
    {
        if(this.applyStatus == null){
            this.applyStatus = ApplyStatusEnum.PENDING;
        }
        this.createdAt = Instant.now();
    }

    @PreUpdate
    public void handleBeforeUpdate() {

        this.setUpdatedBy(JwtUtil.getCurrentUserLogin()
                .orElseThrow(() -> null));
        this.setUpdatedAt(Instant.now());
    }
}
