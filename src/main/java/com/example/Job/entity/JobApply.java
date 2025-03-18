package com.example.Job.entity;

import com.example.Job.constant.ApplyStatusEnum;
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

    private Instant applyDate;

    @ManyToOne
    @JoinColumn(name = "applicant_id")
    @JsonIgnore
    private User applicant;

    @ManyToOne
    @JsonIgnore
    private Job job;

    @PrePersist
    private void beforeCreate()
    {
        if(this.applyStatus == null){
            this.applyStatus = ApplyStatusEnum.PENDING;
        }
        this.applyDate = Instant.now();
    }
}
